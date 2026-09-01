package com.binlicoder.service;

import com.binlicoder.common.api.PageResponse;
import com.binlicoder.dto.DemoItemSaveDTO;
import com.binlicoder.vo.DemoItemVO;

public interface DemoItemService {

    DemoItemVO getById(Long id);

    PageResponse<DemoItemVO> page(long current, long size);

    DemoItemVO create(DemoItemSaveDTO dto);

    DemoItemVO update(Long id, DemoItemSaveDTO dto);

    void delete(Long id);
}
