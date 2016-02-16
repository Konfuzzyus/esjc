package com.github.msemys.esjc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.github.msemys.esjc.util.Throwables;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.github.msemys.esjc.util.Preconditions.checkArgument;
import static com.github.msemys.esjc.util.Preconditions.checkNotNull;
import static com.github.msemys.esjc.util.Strings.isNullOrEmpty;
import static com.github.msemys.esjc.util.Strings.toBytes;

public class SystemSettings {
    private static final Gson gson = new GsonBuilder()
        .registerTypeAdapter(SystemSettings.class, new SystemSettingsJsonAdapter())
        .create();

    public final StreamAcl userStreamAcl;
    public final StreamAcl systemStreamAcl;

    public SystemSettings(StreamAcl userStreamAcl, StreamAcl systemStreamAcl) {
        this.userStreamAcl = userStreamAcl;
        this.systemStreamAcl = systemStreamAcl;
    }

    public String toJson() {
        return gson.toJson(this);
    }

    public static SystemSettings fromJson(String json) {
        checkArgument(!isNullOrEmpty(json), "json");
        return fromJson(toBytes(json));
    }

    public static SystemSettings fromJson(byte[] bytes) {
        checkNotNull(bytes, "bytes");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes)))) {
            return gson.fromJson(new JsonReader(reader), SystemSettings.class);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public String toString() {
        return String.format("UserStreamAcl: (%s), SystemStreamAcl: (%s)", userStreamAcl, systemStreamAcl);
    }
}