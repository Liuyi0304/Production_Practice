package org.example.sign_language.service;

import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.example.sign_language.common.Constants;
import org.example.sign_language.common.enums.ResultCodeEnum;
import org.example.sign_language.common.enums.RoleEnum;
import org.example.sign_language.entity.Account;
import org.example.sign_language.entity.User;
import org.example.sign_language.exception.CustomException;
import org.example.sign_language.mapper.UserMapper;
import org.example.sign_language.utils.TokenUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;
    @Transactional
    public void add(User user) {
        //校验用户是否已存在
        User dbUser = userMapper.selectByUsername(user.getUsername());
        if (ObjectUtil.isNotNull(dbUser)) {
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        if (ObjectUtil.isEmpty(user.getPassword())) {
            user.setPassword(Constants.USER_DEFAULT_PASSWORD);
        }
        user.setRole(RoleEnum.USER.name());
        userMapper.insert(user);
    }
    @Transactional
    public void deleteById(Integer id) {
        userMapper.deleteById(id);
    }
    /**
     *  批量删除
     */
    @Transactional
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            userMapper.deleteById(id);
        }
    }
    @Transactional
    public void updateById(User user) {
        userMapper.updateById(user);
    }
    @Transactional
    public User selectById(Integer id) {
        return userMapper.selectById(id);
    }
    @Transactional
    public List<User> selectAll(User user) {
        return userMapper.selectAll(user);
    }
    /**
     * 分页查询
     */
    @Transactional
    public PageInfo<User> selectPage(User user, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> list = userMapper.selectAll(user);
        return PageInfo.of(list);
    }
    /**
     * 登录
     */
    public Account login(Account account) {
        Account dbUser = userMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbUser) || !dbUser.getRole().equals("USER")) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbUser.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        // 生成token
        String tokenData = dbUser.getId() + "-" + RoleEnum.USER.name();
        String token = TokenUtils.createToken(tokenData, dbUser.getPassword());
        dbUser.setToken(token);
        return dbUser;
    }

    /**
     * 注册
     */
    public void register(Account account) {
        User User = new User();
        BeanUtils.copyProperties(account, User);
        add(User);
    }

    /**
     * 修改密码
     */
    public void updatePassword(Account account) {
        User dbUser = userMapper.selectById(account.getId());
        if (ObjectUtil.isNull(dbUser)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbUser.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
        }
        dbUser.setPassword(account.getNewPassword());
        userMapper.updateById(dbUser);
    }
}
