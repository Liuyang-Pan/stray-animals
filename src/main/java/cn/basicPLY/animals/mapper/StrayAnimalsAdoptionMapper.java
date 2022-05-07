package cn.basicPLY.animals.mapper;

import cn.basicPLY.animals.entity.StrayAnimalsAdoption;
import cn.basicPLY.animals.entity.StrayAnimalsFile;
import cn.basicPLY.animals.entity.VO.StrayAnimalsAdopterVO;
import cn.basicPLY.animals.entity.VO.StrayAnimalsAdoptionVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【stray_animals_adoption(流浪动物救助网：领养信息表)】的数据库操作Mapper
 * @createDate 2022-04-28 22:09:52
 * @Entity cn.basicPLY.animals.entity.StrayAnimalsAdoption
 */
public interface StrayAnimalsAdoptionMapper extends BaseMapper<StrayAnimalsAdoption> {

    /**
     * 分页查询领养信息
     *
     * @param page          分页参数
     * @param userId        用户ID
     * @param adoptionTitle 模糊查询标题
     * @return 返回数据列表
     */
    List<StrayAnimalsAdoptionVO> selectStrayAnimalsAdoptionPageVO(Page<StrayAnimalsAdoptionVO> page,
                                                                  String userId,
                                                                  String adoptionTitle);

    List<StrayAnimalsFile> selectAdoptionFileList(String adoptionId);

    /**
     * 根据领养KeyId查询领养详情
     *
     * @param keyId keyId
     * @return 领养详情
     */
    StrayAnimalsAdoptionVO selectStrayAnimalsAdoptionInfoByKeyId(@Param("keyId") String keyId);

    /**
     * 根据领养信息keyId和领养人领养状态查询领养人相关信息
     *
     * @param keyId 领养信息keyId
     * @return 领养人相关信息
     */
    List<StrayAnimalsAdopterVO> selectStrayAnimalsAdopterList(@Param("keyId") String keyId);

    /**
     * 查询我的领养与申请领养情况相关接口
     *
     * @param page 分页信息
     * @return 领养信息
     */
    List<StrayAnimalsAdoptionVO> selectMyPostAdoption(IPage<StrayAnimalsAdoptionVO> page, @Param("userId") String userId);

    /**
     * 获取我申请的领养信息
     *
     * @param page   分页信息
     * @param userId 用户ID
     * @return 我的领养列表信息
     */
    List<StrayAnimalsAdoption> selectMyAdoptionList(Page<StrayAnimalsAdoption> page, @Param("userId") String userId);
}