package com.devamy.dcombat.border.animation.particle;

import com.devamy.dcombat.border.BorderPoint;
import com.github.retrooper.packetevents.protocol.color.AlphaColor;
import com.github.retrooper.packetevents.protocol.particle.Particle;
import com.github.retrooper.packetevents.protocol.particle.data.ParticleColorData;
import com.github.retrooper.packetevents.protocol.particle.data.ParticleData;
import com.github.retrooper.packetevents.protocol.particle.data.ParticleDustData;
import com.github.retrooper.packetevents.protocol.particle.type.ParticleType;
import com.github.retrooper.packetevents.protocol.particle.type.ParticleTypes;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerParticle;
import eu.okaeri.configs.OkaeriConfig;
import java.awt.Color;

@SuppressWarnings("unchecked")
public class ParticleSettings extends OkaeriConfig {

        public boolean enabled = true;

        public ParticleType type = ParticleTypes.DUST;
        public ParticleColor color = ParticleColor.RAINBOW;
    public int count = 1;
    public float scale = 1.7F;
    public float maxSpeed  = 0.0F;
    public float offsetX = 0.2F;
    public float offsetY = 0.2F;
    public float offsetZ = 0.2F;

    public WrapperPlayServerParticle getParticle(BorderPoint point) {
        return getParticle(point, type);
    }

    private <T extends ParticleData>  WrapperPlayServerParticle getParticle(BorderPoint point, ParticleType<T> type) {
        T particleData = this.createData(type, point);
        Particle<?> dust = new Particle<>(type, particleData);
        return new WrapperPlayServerParticle(
            dust,
            true,
            new Vector3d(point.x(), point.y(), point.z()),
            new Vector3f(orElse(offsetX, 0.1F), orElse(offsetY, 0.1F), orElse(offsetZ, 0.1F)),
            orElse(maxSpeed, 0.0F),
            count,
            true
        );
    }

    private <T> T orElse(T nullable, T or) {
        return nullable != null ? nullable : or;
    }

    @SuppressWarnings("unchecked")
    private <T extends ParticleData> T createData(ParticleType<T> type, BorderPoint point) {
        if (type.equals(ParticleTypes.DUST)) {
            Color color = this.color.getColor(point);
            return (T) new ParticleDustData(orElse(scale, 1.0F), color.getRed(), color.getGreen(), color.getBlue());
        }

        if (type.equals(ParticleTypes.ENTITY_EFFECT)) {
            Color color = this.color.getColor(point);
            AlphaColor alphaColor = new AlphaColor(color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue());
            return (T) new ParticleColorData(alphaColor);
        }

        return ParticleData.emptyData();
    }

}
