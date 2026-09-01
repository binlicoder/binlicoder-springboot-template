package com.binlicoder.controller;

import com.binlicoder.common.api.ApiResponse;
import com.binlicoder.common.api.PageResponse;
import com.binlicoder.dto.DemoItemSaveDTO;
import com.binlicoder.service.DemoItemService;
import com.binlicoder.vo.DemoItemVO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/demo/items")
public class DemoItemController {

    private final DemoItemService service;

    public DemoItemController(DemoItemService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ApiResponse<DemoItemVO> get(@PathVariable @Min(1) Long id) {
        return ApiResponse.success(service.getById(id));
    }

    @GetMapping
    public ApiResponse<PageResponse<DemoItemVO>> page(
            @RequestParam(defaultValue = "1") @Min(1) long current,
            @RequestParam(defaultValue = "20") @Min(1) @Max(500) long size
    ) {
        return ApiResponse.success(service.page(current, size));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DemoItemVO>> create(@Valid @RequestBody DemoItemSaveDTO dto) {
        var body = ApiResponse.success(service.create(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PutMapping("/{id}")
    public ApiResponse<DemoItemVO> update(
            @PathVariable @Min(1) Long id,
            @Valid @RequestBody DemoItemSaveDTO dto
    ) {
        return ApiResponse.success(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable @Min(1) Long id) {
        service.delete(id);
        return ApiResponse.success();
    }
}
