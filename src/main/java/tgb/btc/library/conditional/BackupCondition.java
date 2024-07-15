package tgb.btc.library.conditional;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import tgb.btc.library.constants.enums.properties.IPropertiesPath;

public class BackupCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return IPropertiesPath.FUNCTIONS_PROPERTIES.getBoolean("auto.backup", Boolean.FALSE);
    }
}
