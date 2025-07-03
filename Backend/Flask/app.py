from flask import Flask, Response, jsonify, request
import pickle
import cv2
import mediapipe as mp
import numpy as np
import time
import logging
from langchain_openai import ChatOpenAI
from langchain_core.prompts import ChatPromptTemplate

# Set up logging
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(message)s')

# Load the model
model_dict = pickle.load(open('./model.p', 'rb'))
model = model_dict['model']

# Initialize the video capture
cap = cv2.VideoCapture(0)

# Mediapipe Hands
mp_hands = mp.solutions.hands
mp_drawing = mp.solutions.drawing_utils
mp_drawing_styles = mp.solutions.drawing_styles

hands = mp_hands.Hands(static_image_mode=False, min_detection_confidence=0.5, max_num_hands=1)

# Labels dictionary
labels_dict = {
    0: 'A', 1: 'B', 2: 'C', 3: 'D', 4: 'E', 5: 'F', 6: 'G', 7: 'H',
    8: 'I', 9: 'J', 10: 'K', 11: 'L', 12: 'M', 13: 'N', 14: 'O',
    15: 'P', 16: 'Q', 17: 'R', 18: 'S', 19: 'T', 20: 'U', 21: 'V',
    22: 'W', 23: 'X', 24: 'Y', 25: 'Z', 26: '0', 27: '1', 28: '2',
    29: '3', 30: '4', 31: '5', 32: '6', 33: '7', 34: '8', 35: '9',
    36: 'I love You', 37: 'yes', 38: 'No', 39: 'Hello', 40: 'Thanks',
    41: 'Sorry', 42: 'space'
}

# Variables to track the detected character and delay counter
last_detected_character = None
fixed_character = ""
delayCounter = 0
start_time = time.time()
# 用于存储完整的预测文本
predicted_text = ""

api_key = "sk-3aa25dcd67b4411abfe1eeb7e38eafde"

app = Flask(__name__)


def generate_frames():
    global last_detected_character, fixed_character, delayCounter, start_time, predicted_text
    while True:
        data_aux = []
        x_ = []
        y_ = []

        ret, frame = cap.read()

        if not ret:
            break

        H, W, _ = frame.shape
        frame_rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
        results = hands.process(frame_rgb)

        if results.multi_hand_landmarks:
            for hand_landmarks in results.multi_hand_landmarks:
                mp_drawing.draw_landmarks(
                    frame,
                    hand_landmarks,
                    mp_hands.HAND_CONNECTIONS,
                    mp_drawing_styles.get_default_hand_landmarks_style(),
                    mp_drawing_styles.get_default_hand_connections_style()
                )

            for hand_landmarks in results.multi_hand_landmarks:
                for i in range(len(hand_landmarks.landmark)):
                    x = hand_landmarks.landmark[i].x
                    y = hand_landmarks.landmark[i].y

                    x_.append(x)
                    y_.append(y)

                for i in range(len(hand_landmarks.landmark)):
                    x = hand_landmarks.landmark[i].x
                    y = hand_landmarks.landmark[i].y
                    data_aux.append(x - min(x_))
                    data_aux.append(y - min(y_))

                x1 = int(min(x_) * W) - 10
                y1 = int(min(y_) * H) - 10
                x2 = int(max(x_) * W) - 10
                y2 = int(max(y_) * H) - 10

                # Make prediction using the model
                prediction = model.predict([np.asarray(data_aux)])
                predicted_character = labels_dict[int(prediction[0])]

                # Draw a rectangle and the predicted character on the frame
                cv2.rectangle(frame, (x1, y1), (x2, y2), (0, 0, 0), 4)
                cv2.putText(frame, predicted_character, (x1, y1 - 10), cv2.FONT_HERSHEY_SIMPLEX, 1.3, (0, 0, 0), 3,
                            cv2.LINE_AA)

                current_time = time.time()

                # Timer logic: Check if the predicted character is the same for more than 1 second
                if predicted_character == last_detected_character:
                    if (current_time - start_time) >= 1.0:  # Class fixed after 1 second
                        fixed_character = predicted_character
                        if delayCounter == 0:  # Add character once after it stabilizes for 1 second
                            if fixed_character == 'space':
                                predicted_text += ' '
                            else:
                                predicted_text += fixed_character
                            delayCounter = 1
                else:
                    # Reset the timer when a new character is detected
                    start_time = current_time
                    last_detected_character = predicted_character
                    delayCounter = 0  # Reset delay counter for a new character

        ret, buffer = cv2.imencode('.jpg', frame)
        frame = buffer.tobytes()
        yield (b'--frame\r\n'
               b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n')


# 将大模型的调用封装成方法
def call(question):
    model = "qwen2-72b-instruct"
    llm = ChatOpenAI(
        base_url="https://dashscope.aliyuncs.com/compatible-mode/v1",
        api_key=api_key,
        model=model,
        streaming=True,
    )
    prompt_template = ChatPromptTemplate.from_messages([
        ("system", "你是一个很出色的历史老师，给我讲讲历史人物的故事吧。"),
        ("user", "{input}")
    ])

    # 使用|操作符将prompt_template和llm连接在一起，创建一个chain对象。
    chain = prompt_template | llm
    for token in chain.stream({"input": question}):
        data = token.content
        # web模型的流式输出
        yield data


@app.route('/video_feed')
def video_feed():
    resp = Response(generate_frames(),
                    mimetype='multipart/x-mixed-replace; boundary=frame')
    resp.headers['Access-Control-Allow-Origin'] = '*'  # 允许所有源访问
    return resp


@app.route('/get_prediction')
def get_prediction():
    resp = jsonify({'prediction': predicted_text})
    resp.headers['Access-Control-Allow-Origin'] = '*'  # 允许所有源访问
    return resp


@app.route('/chat', methods=['post'])
def chat():
    data = request.json
    # 接收用户输入的问题
    question = ""
    if (data["question"]):
        question = data["question"]
    print(question)
    return call(question)


if __name__ == '__main__':
    app.run(debug=True)
