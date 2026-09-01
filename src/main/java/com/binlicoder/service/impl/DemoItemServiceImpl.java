package com.binlicoder.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.binlicoder.common.api.PageResponse;
import com.binlicoder.common.error.BusinessException;
import com.binlicoder.common.error.ErrorCode;
import com.binlicoder.dto.DemoItemSaveDTO;
import com.binlicoder.entity.DemoItemEntity;
import com.binlicoder.mapper.DemoItemMapper;
import com.binlicoder.service.DemoItemService;
import com.binlicoder.vo.DemoItemVO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DemoItemServiceImpl implements DemoItemService {

    private final DemoItemMapper mapper;

    public DemoItemServiceImpl(DemoItemMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    @Cacheable(cacheNames = "demo-items", key = "#id")
    @Transactional(readOnly = true)
    public DemoItemVO getById(Long id) {
        DemoItemEntity entity = mapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "示例数据不存在");
        }
        return DemoItemVO.from(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<DemoItemVO> page(long current, long size) {
        Page<DemoItemEntity> page = mapper.selectPage(Page.of(current, size), null);
        return new PageResponse<>(page.getRecords().stream().map(DemoItemVO::from).toList(),
                page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    @Transactional
    public DemoItemVO create(DemoItemSaveDTO dto) {
        DemoItemEntity entity = new DemoItemEntity();
        entity.setName(dto.name());
        entity.setEnabled(dto.enabled());
        mapper.insert(entity);
        return DemoItemVO.from(entity);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "demo-items", key = "#id")
    public DemoItemVO update(Long id, DemoItemSaveDTO dto) {
        DemoItemEntity entity = mapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "示例数据不存在");
        }
        entity.setName(dto.name());
        entity.setEnabled(dto.enabled());
        int affectedRows = mapper.updateById(entity);
        if (affectedRows == 0) {
            throw new BusinessException(ErrorCode.CONFLICT, "数据已被其他请求修改，请刷新后重试");
        }
        return DemoItemVO.from(entity);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "demo-items", key = "#id")
    public void delete(Long id) {
        if (mapper.deleteById(id) == 0) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "示例数据不存在");
        }
    }
}
