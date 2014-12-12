package com.patrickchristensen.qup.util;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.patrickchristensen.qup.commands.Command;

public class SongQueueDeserializer implements JsonDeserializer<Command>{

	@Override
	public Command deserialize(JsonElement je, Type type,
			JsonDeserializationContext jdc) throws JsonParseException {
		JsonElement songQueue = je.getAsJsonObject().get("data");
		return new Gson().fromJson(songQueue, Command.class);
	}

}
