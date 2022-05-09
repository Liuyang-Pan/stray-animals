package cn.basicPLY.animals.mapper;

import cn.basicPLY.animals.entity.StrayAnimalsFile;
import cn.basicPLY.animals.entity.StrayAnimalsResource;
import cn.basicPLY.animals.entity.VO.StrayAnimalsResourceVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【stray_animals_resource(流浪动物救助网：资源需求相关信息表)】的数据库操作Mapper
 * @createDate 2022-05-02 14:54:42
 * @Entity cn.basicPLY.animals.entity.StrayAnimalsResource
 */
public interface StrayAnimalsResourceMapper extends BaseMapper<StrayAnimalsResource> {

    /**
     * 分页获取需求/供应信息
     *
     * @param page     分页参数
     * @param isItMine 是否查询我的发布信息
     * @param userId    当前登录用户ID
     * @return 需求/供应信息列表
     */
    List<StrayAnimalsResourceVO> selectResourcePage(Page<StrayAnimalsResourceVO> page, @Param("resourceType") String resourceType,
                                                    @Param("isItMine") String isItMine, @Param("userId") String userId);

    List<StrayAnimalsFile> selectResourceFileList(String resourceId);

    /**
     * 根据KeyId查询资源信息详情
     *
     * @param keyId KeyId
     * @return 资源信息详情
     */
    StrayAnimalsResourceVO selectResourcesByKeyId(@Param("keyId") String keyId);
}




