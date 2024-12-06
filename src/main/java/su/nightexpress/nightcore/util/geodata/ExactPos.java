package su.nightexpress.nightcore.util.geodata;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.NumberUtil;

import java.util.Objects;

public class ExactPos {

    private final double x,y,z;
    private final float yaw, pitch;

    public ExactPos(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public boolean isEmpty() {
        return this.x == 0 && this.y == 0 && this.z == 0 && this.yaw == 0 && this.pitch == 0;
    }

    @NotNull
    public static ExactPos empty() {
        return new ExactPos(0, 0, 0, 0F, 0F);
    }

    @NotNull
    public static ExactPos from(@NotNull Block block) {
        return from(BlockPos.from(block));
    }

    @NotNull
    public static ExactPos from(@NotNull BlockPos pos) {
        return new ExactPos(pos.getX(), pos.getY(), pos.getZ(), 0F, 0F);
    }

    @NotNull
    public static ExactPos from(@NotNull Location location) {
        return new ExactPos(location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getYaw(), location.getPitch());
    }

    @NotNull
    public static ExactPos read(@NotNull FileConfig cfg, @NotNull String path) {
        String str = cfg.getString(path, "");
        return deserialize(str);
    }

    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.set(path, this.serialize());
    }

    @NotNull
    public static ExactPos deserialize(@NotNull String str) {
        String[] split = str.split(",");
        if (split.length < 3) return empty();

        int x = (int) NumberUtil.getAnyDouble(split[0], 0);
        int y = (int) NumberUtil.getAnyDouble(split[1], 0);
        int z = (int) NumberUtil.getAnyDouble(split[2], 0);
        float pitch = (float) NumberUtil.getAnyDouble(split[3], 0);
        float yaw = (float) NumberUtil.getAnyDouble(split[4], 0);

        return new ExactPos(x, y, z, yaw, pitch);
    }

    @NotNull
    public String serialize() {
        return this.x + "," + this.y + "," + this.z + "," + this.pitch + "," + this.yaw;
    }

    @NotNull
    public Chunk toChunk(@NotNull World world) {
        int chunkX = (int) this.x >> 4;
        int chunkZ = (int) this.z >> 4;

        return world.getChunkAt(chunkX, chunkZ);
    }

    public boolean isChunkLoaded(@NotNull World world) {
        int chunkX = (int) this.x >> 4;
        int chunkZ = (int) this.z >> 4;

        return world.isChunkLoaded(chunkX, chunkZ);
    }

    @NotNull
    public Location toLocation(@NotNull World world) {
        Location location = new Location(world, this.x, this.y, this.z);
        location.setPitch(this.pitch);
        location.setYaw(this.yaw);
        return location;
    }

    @NotNull
    public ExactPos copy() {
        return new ExactPos(this.x, this.y, this.z, this.yaw, this.pitch);
    }

    @NotNull
    public BlockPos toBlockPos() {
        return new BlockPos((int) this.x, (int) this.y, (int) this.z);
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ExactPos other)) return false;
        return x == other.x && y == other.y && z == other.z && Float.compare(yaw, other.yaw) == 0 && Float.compare(pitch, other.pitch) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, yaw, pitch);
    }

    @Override
    public String toString() {
        return "ExactPos{" +
            "x=" + x +
            ", y=" + y +
            ", z=" + z +
            ", yaw=" + yaw +
            ", pitch=" + pitch +
            '}';
    }
}
