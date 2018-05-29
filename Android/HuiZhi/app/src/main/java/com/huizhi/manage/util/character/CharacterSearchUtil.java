package com.huizhi.manage.util.character;

import android.text.TextUtils;

import com.huizhi.manage.node.UserNode;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by CL on 2018/2/10.
 */

public class CharacterSearchUtil {

    public static List<UserNode> searchUsers(CharSequence str, List<UserNode> users){
        List<UserNode> nodes = new ArrayList<>();
        if (str.toString().startsWith("0") || str.toString().startsWith("1")
                || str.toString().startsWith("+")) {
            for(UserNode user:users){
                if(getUserName(user)!=null && user.getTeacherName() + "" != null){
                    if((user.getTeacherName()+"").contains(str)){
                        nodes.add(user);
                    }
                }
            }
            return nodes;
        }
        CharacterParser finder = CharacterParser.getInstance();
        String result = "";
        for(UserNode user:users){
            finder.setResource(str.toString());
            result = finder.getSpelling();
            if(contains(user, result)){
                nodes.add(user);
            }else if((user.getTeacherName()+"").contains(str)){
                nodes.add(user);
            }
        }
        return nodes;
    }

    private static String getUserName(UserNode node){
        String strName = null;
        if (!TextUtils.isEmpty(node.getTeacherName())) {
            strName = node.getTeacherName();
        } else if (!TextUtils.isEmpty(node.getTeacherName())) {
            strName = node.getTeacherName();
        } else {
            strName = "";
        }

        return strName;
    }

    private static boolean contains(UserNode group, String search) {
        if (TextUtils.isEmpty(group.getTeacherName())
                && TextUtils.isEmpty(group.getTeacherName())) {
            return false;
        }

        boolean flag = false;

        // 简拼匹配,如果输入在字符串长度大于6就不按首字母匹配了
        if (search.length() < 6) {
            String firstLetters = FirstLetterUtil.getFirstLetter(getUserName(group));
            // 不区分大小写
            Pattern firstLetterMatcher = Pattern.compile(search,
                    Pattern.CASE_INSENSITIVE);
            flag = firstLetterMatcher.matcher(firstLetters).find();
        }

        if (!flag) { // 如果简拼已经找到了，就不使用全拼了
            // 全拼匹配
            CharacterParser finder = CharacterParser.getInstance();
            finder.setResource(getUserName(group));
            // 不区分大小写
            Pattern pattern2 = Pattern
                    .compile(search, Pattern.CASE_INSENSITIVE);
            Matcher matcher2 = pattern2.matcher(finder.getSpelling());
            flag = matcher2.find();
        }

        return flag;
    }
}
