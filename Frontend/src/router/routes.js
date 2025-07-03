const routes = [
    {
        path: '/',
        name: 'index',
        title: '首页',
        component: () => import('../components/Home.vue'), //.vue不能省略
        children: [
            {path:'dictionary',component: () => import('../components/Dictionary.vue') },
            {path:'prediction',component:()=>import('../components/Prediction.vue')},
            {path:'sign_translation',component:()=>import('../components/SignTranslation.vue')},
            {path:'person',component:()=>import('../components/Person.vue')}
        ]
    },
    { path: '/login', name: 'Login', meta: {name: '登录' }, component: () => import('../components/Login.vue') },
    { path: '/register', name: 'Register', meta: {name: '注册' }, component: () => import('../components/Register.vue') },
    {path:'/index',name: "Index",  component: () => import('../components/Index.vue')}
]
export default routes
