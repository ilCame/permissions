package org.example.permission.permissions.security;

import com.yahoo.elide.annotation.SecurityCheck;
import com.yahoo.elide.core.security.ChangeSpec;
import com.yahoo.elide.core.security.RequestScope;
import com.yahoo.elide.core.security.checks.OperationCheck;
import org.apache.commons.lang3.StringUtils;
import org.example.permission.permissions.domain.CodeAwareEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@SecurityCheck("permission Create")
public class CodeOperationCheck extends OperationCheck<CodeAwareEntity> {

    @Override
    public boolean ok(CodeAwareEntity object, RequestScope requestScope, Optional< ChangeSpec > changeSpec) {

        Set<String> codesHeaders =
                Optional.ofNullable(requestScope.getRoute().getHeaders())
                        .map(headers -> headers.get("X-Code-Permission"))
                        .flatMap(headers -> headers.stream().findFirst())
                        .filter(StringUtils::isNotBlank)
                        .map(header -> Set.of(header.toLowerCase().split(",")))
                        .orElse(Set.of());

        String objectCode = object.getCode();
        return codesHeaders.contains(objectCode);
    }
}
