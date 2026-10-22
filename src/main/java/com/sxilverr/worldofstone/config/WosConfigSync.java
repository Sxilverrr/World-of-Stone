package com.sxilverr.worldofstone.config;

import com.sxilverr.worldofstone.api.ModInfo;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import dev.architectury.utils.GameInstance;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArrayList;

public final class WosConfigSync {

    private static final ResourceLocation CHANNEL = ModInfo.rl("config_sync");

    private static final byte BOOL = 0;
    private static final byte INT = 1;
    private static final byte DOUBLE = 2;
    private static final byte ENUM = 3;
    private static final byte STRINGS = 4;
    private static final byte MAP = 5;

    private static final Map<String, Field> FIELDS = new LinkedHashMap<>();
    private static final List<Runnable> LISTENERS = new CopyOnWriteArrayList<>();

    private static volatile byte[] remote;

    static {
        for (Field field : WosConfig.class.getDeclaredFields()) {
            int modifiers = field.getModifiers();
            if (Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers) && !Modifier.isTransient(modifiers)) {
                FIELDS.put(field.getName(), field);
            }
        }
    }

    public static void init() {
        boolean client = Platform.getEnvironment() == Env.CLIENT;
        if (client) {
            NetworkManager.registerReceiver(NetworkManager.Side.S2C, CHANNEL, (buf, context) -> {
                byte[] data = new byte[buf.readableBytes()];
                buf.readBytes(data);
                context.queue(() -> acceptRemote(data));
            });
            WosConfigSyncClient.init();
        }
        //? if >=1.21.1 {
        /*if (!client) NetworkManager.registerS2CPayloadType(CHANNEL);
        *///?}
        PlayerEvent.PLAYER_JOIN.register(WosConfigSync::sendTo);
    }

    public static void addListener(Runnable listener) {
        LISTENERS.add(listener);
    }

    public static void afterBake() {
        if (remote != null) {
            applyRemote();
            return;
        }
        MinecraftServer server = GameInstance.getServer();
        if (server != null) {
            server.execute(() -> {
                for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                    sendTo(player);
                }
            });
        }
    }

    static void clearRemote() {
        byte[] previous = remote;
        if (previous == null) return;
        remote = null;
        WosConfigSchema.bake();
        if (!Arrays.equals(previous, snapshot())) notifyListeners();
    }

    private static void sendTo(ServerPlayer player) {
        MinecraftServer server = player.getServer();
        if (server == null || server.isSingleplayerOwner(player.getGameProfile())) return;
        //? if >=1.21.1 {
        /*net.minecraft.network.RegistryFriendlyByteBuf buf = new net.minecraft.network.RegistryFriendlyByteBuf(Unpooled.buffer(), player.registryAccess());
        *///?} else {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        //?}
        encode(buf);
        NetworkManager.sendToPlayer(player, CHANNEL, buf);
    }

    private static void acceptRemote(byte[] data) {
        byte[] previous = snapshot();
        remote = data;
        applyRemote();
        if (!Arrays.equals(previous, data)) notifyListeners();
    }

    private static void applyRemote() {
        byte[] data = remote;
        if (data != null) decode(new FriendlyByteBuf(Unpooled.wrappedBuffer(data)));
    }

    private static void notifyListeners() {
        for (Runnable listener : LISTENERS) {
            listener.run();
        }
    }

    private static byte[] snapshot() {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        encode(buf);
        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);
        buf.release();
        return data;
    }

    private static void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(FIELDS.size());
        for (Field field : FIELDS.values()) {
            buf.writeUtf(field.getName());
            Object value = read(field);
            if (value instanceof Boolean flag) {
                buf.writeByte(BOOL);
                buf.writeBoolean(flag);
            } else if (value instanceof Integer number) {
                buf.writeByte(INT);
                buf.writeVarInt(number);
            } else if (value instanceof Double number) {
                buf.writeByte(DOUBLE);
                buf.writeDouble(number);
            } else if (value instanceof Enum<?> constant) {
                buf.writeByte(ENUM);
                buf.writeUtf(constant.name());
            } else if (value instanceof Collection<?> collection) {
                Collection<?> ordered = collection instanceof Set<?> ? new TreeSet<>(collection) : collection;
                buf.writeByte(STRINGS);
                buf.writeVarInt(ordered.size());
                for (Object element : ordered) {
                    buf.writeUtf(String.valueOf(element));
                }
            } else if (value instanceof Map<?, ?> map) {
                buf.writeByte(MAP);
                buf.writeVarInt(map.size());
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    buf.writeUtf(((Enum<?>) entry.getKey()).name());
                    Object element = entry.getValue();
                    buf.writeVarInt(element instanceof Boolean flag ? (flag ? 1 : 0) : (Integer) element);
                }
            } else {
                throw new IllegalStateException("Unsupported config field " + field.getName());
            }
        }
    }

    private static void decode(FriendlyByteBuf buf) {
        int count = buf.readVarInt();
        for (int i = 0; i < count; i++) {
            Field field = FIELDS.get(buf.readUtf());
            switch (buf.readByte()) {
                case BOOL -> write(field, buf.readBoolean());
                case INT -> write(field, buf.readVarInt());
                case DOUBLE -> write(field, buf.readDouble());
                case ENUM -> {
                    String name = buf.readUtf();
                    if (field != null) write(field, constant(field.getType(), name));
                }
                case STRINGS -> {
                    int size = buf.readVarInt();
                    List<String> values = new ArrayList<>(size);
                    for (int j = 0; j < size; j++) {
                        values.add(buf.readUtf());
                    }
                    if (field != null) {
                        write(field, Set.class.isAssignableFrom(field.getType()) ? new HashSet<>(values) : values);
                    }
                }
                case MAP -> {
                    int size = buf.readVarInt();
                    Map<String, Integer> values = new LinkedHashMap<>();
                    for (int j = 0; j < size; j++) {
                        String key = buf.readUtf();
                        values.put(key, buf.readVarInt());
                    }
                    if (field != null) fill(field, values);
                }
                default -> throw new IllegalStateException("Unknown config sync tag");
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static void fill(Field field, Map<String, Integer> values) {
        if (!(read(field) instanceof Map<?, ?> existing) || !(field.getGenericType() instanceof ParameterizedType type)) return;
        Type[] arguments = type.getActualTypeArguments();
        if (!(arguments[0] instanceof Class<?> keyType)) return;
        boolean flags = arguments[1] == Boolean.class;
        Map<Object, Object> target = (Map<Object, Object>) existing;
        target.clear();
        for (Map.Entry<String, Integer> entry : values.entrySet()) {
            Object key = constant(keyType, entry.getKey());
            if (key == null) continue;
            target.put(key, flags ? (Object) (entry.getValue() != 0) : (Object) entry.getValue());
        }
    }

    private static Object constant(Class<?> type, String name) {
        Object[] constants = type.getEnumConstants();
        if (constants == null) return null;
        for (Object candidate : constants) {
            if (((Enum<?>) candidate).name().equals(name)) return candidate;
        }
        return null;
    }

    private static Object read(Field field) {
        try {
            return field.get(null);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void write(Field field, Object value) {
        if (field == null || value == null) return;
        try {
            field.set(null, value);
        } catch (IllegalAccessException | IllegalArgumentException ignored) {
        }
    }

    private WosConfigSync() {
    }
}
