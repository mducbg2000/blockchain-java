package utils;

import com.google.gson.*;
import org.bouncycastle.util.encoders.Hex;

import java.lang.reflect.Type;

public class GsonUtil {

	private static final Gson gson = new GsonBuilder()
			.setPrettyPrinting()
			.registerTypeAdapter(byte[].class, new ByteArrayToHexAdapter())
			.create();

	private static class ByteArrayToHexAdapter implements JsonSerializer<byte[]>, JsonDeserializer<byte[]> {
		@Override
		public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
			return new JsonPrimitive(Hex.toHexString(src));
		}

		@Override
		public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			return Hex.decode(json.getAsString());
		}
	}

	public static Gson getGson() {
		return gson;
	}

}
