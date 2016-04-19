/*
 * Copyright (C) 2016 to the original authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package io.fabric8.spring.cloud.kubernetes.config;

import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;

@Order(0)
public class ConfigMapPropertySourceLocator implements PropertySourceLocator {

    private static final String SPRING_APPLICATION_NAME = "spring.application.name";

    private final KubernetesClient client;
    private final ConfigMapConfigProperties properties;

    public ConfigMapPropertySourceLocator(KubernetesClient client, ConfigMapConfigProperties properties) {
        this.client = client;
        this.properties = properties;
    }

    @Override
    public PropertySource<?> locate(Environment environment) {
        if (environment instanceof ConfigurableEnvironment) {
            ConfigurableEnvironment env = (ConfigurableEnvironment) environment;
            String appName = env.getProperty(SPRING_APPLICATION_NAME);
            String name = properties.getName() == null || properties.getName().isEmpty() ? appName : properties.getName();
            String namespace = properties.getNamespace();
            return new ConfigMapPropertySource(client, name, namespace);
        }
        return null;
    }
}