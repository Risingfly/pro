package com.genge.focus.dao;

import com.genge.focus.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserDAO {
    String TABLE_NAME = "`user`";
    String INSERT_FIELDS= "`id`,`name`,`password`,`salt`,`headUrl`";
    String SELECT_FIELDS = "id,name,password,salt,headUrl";

    @Insert({"insert into user (`id`,`name`,`password`,`salt`,`headurl`) values (#{id},#{name},#{password},#{salt},#{headUrl})"})
    int addUser(User user);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where id=#{id}"})
    User selectById(int id);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where name=#{name}"})
    User selectByName(String name);

    @Update({"update",TABLE_NAME,"set password=#{password} where id=#{id}"})
    void update(User user);

    @Delete({"delete from",TABLE_NAME,"where id=#{id}"})
    void deleteById(int id);

}
