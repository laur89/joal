package org.araymond.joal.core.client.emulated.generator.peerid.generation;

import org.araymond.joal.core.client.emulated.generator.peerid.PeerIdGenerator;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

public class RandomPoolWithChecksumPeerIdAlgorithmTest {

    @Test
    public void shouldGenerateProperPeerIds() {
        final String prefix = "-TR2820-";
        final RandomPoolWithChecksumPeerIdAlgorithm algo = Mockito.spy(new RandomPoolWithChecksumPeerIdAlgorithm(prefix, "0123456789abcdefghijklmnopqrstuvwxyz", 36));
        Mockito
                .doReturn(new byte[]{(byte) 250, (byte) 250, (byte) 250, (byte) 250, (byte) 250, (byte) 250, (byte) 250, (byte) 250, (byte) 250, (byte) 250, (byte) 250})
                .doReturn(new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0})
                .doReturn(new byte[]{(byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255})
                .doReturn(new byte[]{(byte) 128, (byte) 128, (byte) 128, (byte) 128, (byte) 128, (byte) 128, (byte) 128, (byte) 128, (byte) 128, (byte) 128, (byte) 128})
                .doReturn(new byte[]{(byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1})
                .doReturn(new byte[]{(byte) 26, (byte) 200, (byte) 124, (byte) 39, (byte) 84, (byte) 248, (byte) 3, (byte) 159, (byte) 64, (byte) 239, (byte) 0})
                .when(algo).generateRandomBytes(Matchers.eq(PeerIdGenerator.PEER_ID_LENGTH - prefix.length() - 1));

        assertThat(algo.generate()).isEqualTo("-TR2820-yyyyyyyyyyym");
        assertThat(algo.generate()).isEqualTo("-TR2820-000000000000");
        assertThat(algo.generate()).isEqualTo("-TR2820-333333333333");
        assertThat(algo.generate()).isEqualTo("-TR2820-kkkkkkkkkkkw");
        assertThat(algo.generate()).isEqualTo("-TR2820-11111111111p");
        assertThat(algo.generate()).isEqualTo("-TR2820-qkg3cw3fsn02");
    }

    @Test
    public void shouldGenerateRandomBytes() {
        final String prefix = "-TR2820-";
        final RandomPoolWithChecksumPeerIdAlgorithm algo = new RandomPoolWithChecksumPeerIdAlgorithm(prefix, "0123456789abcdefghijklmnopqrstuvwxyz", 36);

        assertThat(algo.generateRandomBytes(20)).hasSize(20);
        assertThat(algo.generateRandomBytes(50)).isNotEqualTo(algo.generateRandomBytes(50));
    }

    @Test
    public void shouldBuild() {
        final String prefix = "-TR2820-";
        final RandomPoolWithChecksumPeerIdAlgorithm algo = new RandomPoolWithChecksumPeerIdAlgorithm(prefix, "0123456789abcdefghijklmnopqrstuvwxyz", 36);

        assertThat(algo.getPrefix()).isEqualTo(prefix);
        assertThat(algo.getCharactersPool()).isEqualTo("0123456789abcdefghijklmnopqrstuvwxyz");
        assertThat(algo.getBase()).isEqualTo(36);
    }

    @Test
    public void shouldRefreshRandomSpeedOneInAWhile() {
        final String prefix = "-TR2820-";
        final RandomPoolWithChecksumPeerIdAlgorithm algo = Mockito.spy(new RandomPoolWithChecksumPeerIdAlgorithm(prefix, "0123456789abcdefghijklmnopqrstuvwxyz", 36));
        for (int i = 0; i < 51; i++) {
            algo.generateRandomBytes(10);
        }

        Mockito.verify(algo, Mockito.atLeast(1)).createSecureRandomSeed();
    }

}
