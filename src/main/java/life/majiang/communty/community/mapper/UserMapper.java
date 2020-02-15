package life.majiang.communty.community.mapper;


import life.majiang.communty.community.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {

    @Insert("INSERT INTO USER (nickname,accountId,token,gmt_create,gmt_modified)VALUES(#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified})")
    void  insert(User user);

    @Select("SELECT * from user where token = #{token}")
    User findByToken(@Param("token") String token);
    @Select("SELECT * from user where token = #{accountId}")
    User findById(@Param("accountId") String accountId);
    @Update("update user set nickname = #{name},gmt_modified = #{gmtModified}  where accountId = #{accountId}")
    void updateUser(User user);
}
