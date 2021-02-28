package ru.apolyakov.conf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Component
@ConfigurationProperties("songs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SongsProperties implements Serializable {
    private List<String> garbage;
}
