package cn.basicPLY.animals.service;

import cn.basicPLY.animals.entity.StrayAnimalsAdoption;
import cn.basicPLY.animals.entity.VO.StrayAnimalsAdoptionVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【stray_animals_adoption(流浪动物救助网：领养信息表)】的数据库操作Service
 * @createDate 2022-04-28 22:09:52
 */
public interface StrayAnimalsAdoptionService extends IService<StrayAnimalsAdoption> {

    /**
     * 分页查询领养信息
     *
     * @param page            分页参数
     * @param adoptionTitle   模糊查询标题
     * @param adoptionContent 模糊查询描述内容
     * @param adoptionAddress 模糊查询标题
     * @return 返回数据列表
     */
    List<StrayAnimalsAdoptionVO> selectStrayAnimalsAdoptionPageVO(Page<StrayAnimalsAdoptionVO> page,
                                                                  String adoptionTitle,
                                                                  String adoptionContent,
                                                                  String adoptionAddress);

    /**
     * 根据领养KeyId查询领养详情
     *
     * @param keyId keyId
     * @return 领养详情
     */
    StrayAnimalsAdoptionVO selectStrayAnimalsAdoptionInfoByKeyId(String keyId);

    /**
     * 获取我的发布领养信息
     *
     * @param page   分页
     * @param userId 用户ID
     * @return 领养信息
     */
    List<StrayAnimalsAdoptionVO> selectMyPostAdoption(IPage<StrayAnimalsAdoptionVO> page, String userId);

    /**
     * 获取我申请的领养信息
     *
     * @param page   分页信息
     * @param userId 用户ID
     * @return 我的领养列表信息
     */
    List<StrayAnimalsAdoption> selectMyAdoptionList(Page<StrayAnimalsAdoption> page, String userId);
}
