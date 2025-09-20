package org.acme;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

import java.util.Optional;

/**
 * Configuration class for the <code>movies</code> prefix.
 */
@ConfigMapping(prefix = "movies")
public interface AppConfig {


	Hello hello();

	interface Hello {

		@WithDefault("Hello Movie")
		String message();
	}
}