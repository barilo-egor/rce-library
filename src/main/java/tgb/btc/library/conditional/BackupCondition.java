package tgb.btc.library.conditional;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import tgb.btc.library.service.properties.FunctionsPropertiesReader;

import java.util.Objects;

public class BackupCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return Objects.requireNonNull(context.getBeanFactory()).getBean(FunctionsPropertiesReader.class)
                .getBoolean("auto.backup", Boolean.FALSE);
    }
}
