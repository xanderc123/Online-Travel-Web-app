package cn.kmbeast.service.impl;

import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.mapper.AttractionMapper;
import cn.kmbeast.mapper.SupplierMapper;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.AttractionQueryDto;
import cn.kmbeast.pojo.dto.query.extend.SupplierQueryDto;
import cn.kmbeast.pojo.em.RoleEnum;
import cn.kmbeast.pojo.entity.Attraction;
import cn.kmbeast.pojo.vo.AttractionVO;
import cn.kmbeast.pojo.vo.SelectedVO;
import cn.kmbeast.pojo.vo.SupplierVO;
import cn.kmbeast.service.AttractionService;
import cn.kmbeast.utils.TextUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 景点的业务逻辑实现类
 */
@Service
public class AttractionServiceImpl implements AttractionService {

    @Resource
    private AttractionMapper attractionMapper;
    @Resource
    private SupplierMapper supplierMapper;

    /**
     * 景点新增
     *
     * @param attraction 实体
     * @return Result<Void>
     */
    @Override
    public Result<Void> save(Attraction attraction) {
        // 景点初始可用状态
        attraction.setStatus(true);
        // 设置初始时间
        attraction.setCreateTime(LocalDateTime.now());
        SupplierQueryDto supplierQueryDto = new SupplierQueryDto();
        // 如果是管理员，直接新增，供应商以及其他信息是已经传进来的
        if (Objects.equals(LocalThreadHolder.getRoleId(), RoleEnum.ADMIN.getRole())) {
            attractionMapper.save(attraction);
            return ApiResult.success();
        }
        // 这是用户自己新增的
        supplierQueryDto.setUserId(LocalThreadHolder.getUserId());
        List<SupplierVO> supplierVOS = supplierMapper.query(supplierQueryDto);
        // 1. 要么真的没有 2. 有的话，只有一项
        if (supplierVOS.isEmpty()) {
            return ApiResult.error("供应商身份异常");
        }
        attraction.setSupplierId(supplierVOS.get(0).getId());
        attractionMapper.save(attraction);
        return ApiResult.success();
    }

    /**
     * 景点修改
     *
     * @param attraction 实体
     * @return Result<Void>
     */
    @Override
    public Result<Void> update(Attraction attraction) {
        attractionMapper.update(attraction);
        return ApiResult.success();
    }

    /**
     * 景点删除
     *
     * @param ids 景点ID列表
     * @return Result<Void>
     */
    @Override
    public Result<Void> batchDelete(List<Integer> ids) {
        attractionMapper.batchDelete(ids);
        return ApiResult.success();
    }

    /**
     * 景点查询
     *
     * @param dto 查询实体
     * @return Result<AttractionVO>
     */
    @Override
    public Result<List<AttractionVO>> query(AttractionQueryDto dto) {
        Integer totalCount = attractionMapper.queryCount(dto);
        List<AttractionVO> result = attractionMapper.query(dto);
        return ApiResult.success(result, totalCount);
    }

    /**
     * 供应商查询自己名下的景点信息
     *
     * @param dto 查询参数
     * @return Result<List < AttractionVO>>
     */
    @Override
    public Result<List<AttractionVO>> querySupplierAttraction(AttractionQueryDto dto) {
        SupplierQueryDto supplierQueryDto = new SupplierQueryDto();
        supplierQueryDto.setUserId(LocalThreadHolder.getUserId());
        List<SupplierVO> supplierVOS = supplierMapper.query(supplierQueryDto);
        // 1. 要么真的没有 2. 有的话，只有一项
        if (supplierVOS.isEmpty()) {
            return ApiResult.error("供应商身份异常");
        }
        dto.setSupplierId(supplierVOS.get(0).getId());
        return query(dto);
    }

    /**
     * 关联景点下拉选择器
     *
     * @return Result<List < SelectedVO>>
     */
    @Override
    public Result<List<SelectedVO>> querySelectedAttraction() {
        AttractionQueryDto attractionQueryDto = new AttractionQueryDto();
        // 只能查可用状态的景点信息
        attractionQueryDto.setStatus(true);
        List<AttractionVO> attractionVOS = attractionMapper.query(attractionQueryDto);
        // 再次封装
        List<SelectedVO> selectedAttractionList = attractionVOS.stream()
                .map(attractionVO -> new SelectedVO(
                        attractionVO.getId(),
                        attractionVO.getName()
                )).collect(Collectors.toList());
        return ApiResult.success(selectedAttractionList);
    }

    /**
     * 浏览操作
     *
     * @param attractionId 景点ID
     * @return Result<Void>
     */
    @Override
    public Result<Void> viewOperation(Integer attractionId) {
        // 第一步，根据景点ID，查询回来景点的信息
        AttractionQueryDto attractionQueryDto = new AttractionQueryDto();
        attractionQueryDto.setId(attractionId);
        List<AttractionVO> attractionVOS = attractionMapper.query(attractionQueryDto);
        if (attractionVOS.isEmpty()) {
            return ApiResult.success();
        }
        AttractionVO attractionVO = attractionVOS.get(0);
        String viewIds = attractionVO.getViewIds();
        // 已经存在，不需要处理了
        if (TextUtils.exitId(viewIds, LocalThreadHolder.getUserId())) {
            return ApiResult.success();
        }
        String newViewIds = TextUtils.join(viewIds, LocalThreadHolder.getUserId());
        // 填充新的信息
        Attraction attraction = new Attraction();
        attraction.setId(attractionId);
        attraction.setViewIds(newViewIds);
        // 修改
        attractionMapper.update(attraction);
        return ApiResult.success();
    }

    /**
     * 收藏操作 --- 收藏跟取消收藏是对立的
     *
     * @param attractionId 景点ID
     * @return Result<Void>
     */
    @Override
    public Result<Void> saveOperation(Integer attractionId) {
        // 第一步，根据景点ID，查询回来景点的信息
        AttractionQueryDto attractionQueryDto = new AttractionQueryDto();
        attractionQueryDto.setId(attractionId);
        List<AttractionVO> attractionVOS = attractionMapper.query(attractionQueryDto);
        if (attractionVOS.isEmpty()) {
            return ApiResult.success();
        }
        AttractionVO attractionVO = attractionVOS.get(0);
        String saveIds = attractionVO.getFavoriteIds();
        Attraction attraction = new Attraction();
        attraction.setId(attractionVO.getId());
        Integer userId = LocalThreadHolder.getUserId();
        // 最后都是更新，只是对于收藏用户ID列表（saveIds）字段的处理不一样
        // 实现方式1
        attraction.setFavoriteIds(
                TextUtils.exitId(saveIds, userId) ?
                        TextUtils.split(saveIds, userId) :
                        TextUtils.join(saveIds, userId)
        );
        // 实现方式2
        // 取消收藏操作
        if (TextUtils.exitId(saveIds, LocalThreadHolder.getUserId())) {
            attraction.setFavoriteIds(TextUtils.split(saveIds, userId));
        } else { // 收藏操作
            attraction.setFavoriteIds(TextUtils.join(saveIds, userId));
        }
        // 更新字段
        attractionMapper.updateSaveIds(attraction);
        return ApiResult.success(TextUtils.exitId(saveIds, userId) ? "Cancel collection successful" : "Collection successful");
    }

    /**
     * 查询用户的收藏景点状况
     *
     * @return Result<Void>
     */
    @Override
    public Result<Boolean> saveStatus(Integer attractionId) {
        // 第一步，根据景点ID，查询回来景点的信息
        AttractionQueryDto attractionQueryDto = new AttractionQueryDto();
        attractionQueryDto.setId(attractionId);
        List<AttractionVO> attractionVOS = attractionMapper.query(attractionQueryDto);
        if (attractionVOS.isEmpty()) {
            return ApiResult.success();
        }
        AttractionVO attractionVO = attractionVOS.get(0);
        String saveIds = attractionVO.getFavoriteIds();
        return ApiResult.success(TextUtils.exitId(saveIds, LocalThreadHolder.getUserId()));
    }

    /**
     * 查询用户收藏的景点信息
     *
     * @return Result<List < AttractionVO>>
     */
    @Override
    public Result<List<AttractionVO>> querySave() {
        AttractionQueryDto attractionQueryDto = new AttractionQueryDto();
        // 设置用户ID
        Integer userId = LocalThreadHolder.getUserId();
        attractionQueryDto.setSaveIds(String.valueOf(userId));
        List<AttractionVO> attractionVOS = attractionMapper.query(attractionQueryDto);
        return ApiResult.success(attractionVOS);
    }
}
