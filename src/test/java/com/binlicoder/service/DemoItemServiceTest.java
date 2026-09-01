package com.binlicoder.service;

import com.binlicoder.common.error.BusinessException;
import com.binlicoder.common.error.ErrorCode;
import com.binlicoder.dto.DemoItemSaveDTO;
import com.binlicoder.entity.DemoItemEntity;
import com.binlicoder.mapper.DemoItemMapper;
import com.binlicoder.service.impl.DemoItemServiceImpl;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DemoItemServiceTest {

    @Test
    void shouldCreateTrimmedItem() {
        DemoItemMapper mapper = mapper((method, argument) -> {
            if (method.equals("insert")) {
                ((DemoItemEntity) argument).setId(1L);
                return 1;
            }
            return null;
        });
        DemoItemService service = new DemoItemServiceImpl(mapper);

        var item = service.create(new DemoItemSaveDTO("  example  ", true));

        assertThat(item.id()).isEqualTo(1L);
        assertThat(item.name()).isEqualTo("example");
        assertThat(item.enabled()).isTrue();
    }

    @Test
    void shouldRejectConcurrentUpdate() {
        DemoItemEntity entity = entity(1L, "before");
        DemoItemMapper mapper = mapper((method, argument) -> switch (method) {
            case "selectById" -> entity;
            case "updateById" -> 0;
            default -> null;
        });
        DemoItemService service = new DemoItemServiceImpl(mapper);

        assertThatThrownBy(() -> service.update(1L, new DemoItemSaveDTO("after", true)))
                .isInstanceOfSatisfying(BusinessException.class,
                        exception -> assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.CONFLICT));
    }

    @Test
    void shouldFailWhenDeletingMissingItem() {
        AtomicReference<Object> deletedId = new AtomicReference<>();
        DemoItemMapper mapper = mapper((method, argument) -> {
            if (method.equals("deleteById")) {
                deletedId.set(argument);
                return 0;
            }
            return null;
        });
        DemoItemService service = new DemoItemServiceImpl(mapper);

        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOfSatisfying(BusinessException.class,
                        exception -> assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND));
        assertThat(deletedId.get()).isEqualTo(99L);
    }

    private static DemoItemMapper mapper(MapperInvocation invocation) {
        return (DemoItemMapper) Proxy.newProxyInstance(
                DemoItemMapper.class.getClassLoader(),
                new Class<?>[]{DemoItemMapper.class},
                (proxy, method, arguments) -> {
                    if (method.getDeclaringClass() == Object.class) {
                        return switch (method.getName()) {
                            case "toString" -> "DemoItemMapperStub";
                            case "hashCode" -> System.identityHashCode(proxy);
                            case "equals" -> proxy == arguments[0];
                            default -> null;
                        };
                    }
                    Object argument = arguments == null || arguments.length == 0 ? null : arguments[0];
                    return invocation.invoke(method.getName(), argument);
                });
    }

    private static DemoItemEntity entity(Long id, String name) {
        DemoItemEntity entity = new DemoItemEntity();
        entity.setId(id);
        entity.setName(name);
        entity.setEnabled(true);
        return entity;
    }

    @FunctionalInterface
    private interface MapperInvocation {
        Object invoke(String method, Object argument);
    }
}
