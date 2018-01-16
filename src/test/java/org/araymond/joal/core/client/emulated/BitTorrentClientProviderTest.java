package org.araymond.joal.core.client.emulated;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.araymond.joal.core.SeedManager;
import org.araymond.joal.core.config.JoalConfigProvider;
import org.araymond.joal.core.config.JoalConfigProviderTest;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Created by raymo on 23/04/2017.
 */
public class BitTorrentClientProviderTest {

    private static final SeedManager.JoalFoldersPath joalFoldersPath = new SeedManager.JoalFoldersPath(Paths.get("src/test/resources/configtest"));

    private static BitTorrentClientProvider createProvider() {
        final JoalConfigProvider configProvider = Mockito.mock(JoalConfigProvider.class);
        Mockito.when(configProvider.get()).thenReturn(JoalConfigProviderTest.defaultConfig);
        return new BitTorrentClientProvider(configProvider, new ObjectMapper(), joalFoldersPath);
    }

    @Test
    public void shouldFailIfClientFileDoesNotExists() {
        final JoalConfigProvider configProvider = Mockito.mock(JoalConfigProvider.class);
        Mockito.when(configProvider.get()).thenReturn(JoalConfigProviderTest.defaultConfig);
        final BitTorrentClientProvider provider = new BitTorrentClientProvider(configProvider, new ObjectMapper(), new SeedManager.JoalFoldersPath(Paths.get("nop")));

        assertThatThrownBy(provider::generateNewClient)
                .isInstanceOf(FileNotFoundException.class)
                .hasMessageContaining("BitTorrent client configuration file");
    }

    @Test
    public void shouldReturnSameClientEveryTimes() throws FileNotFoundException {
        final BitTorrentClientProvider provider = createProvider();

        assertThat(provider.get())
                .isEqualToComparingFieldByField(provider.get())
                .isEqualToComparingFieldByField(provider.get())
                .isEqualToComparingFieldByField(provider.get())
                .isEqualToComparingFieldByField(provider.get());
    }

    @Test
    public void shouldChangeClientEveryTimesGenerateIsCalled() throws FileNotFoundException {
        final BitTorrentClientProvider provider = createProvider();

        assertThat(provider.get())
                .isEqualToComparingFieldByField(provider.get())
                .isEqualToComparingFieldByField(provider.get())
                .isEqualToComparingFieldByField(provider.get())
                .isEqualToComparingFieldByField(provider.get());
    }

    @Test
    public void shouldGetClient() throws FileNotFoundException {
        final BitTorrentClientProvider provider = createProvider();

        assertThat(provider.get()).isNotNull();
    }

    @Test
    public void shouldistClientFiles() {
        final BitTorrentClientProvider provider = new BitTorrentClientProvider(Mockito.mock(JoalConfigProvider.class), new ObjectMapper(), joalFoldersPath);

        final List<String> clientFiles = provider.listClientFiles();
        assertThat(clientFiles).hasSize(1);
        assertThat(clientFiles.get(0)).isEqualTo("azureus-5.7.5.0.client");
    }


}
