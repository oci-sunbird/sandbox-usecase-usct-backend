package global.govstack.mocksris.configuration;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "callback")
public record CallbackProperties(List<String> cidr) {}
