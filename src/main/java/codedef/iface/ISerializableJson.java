package codedef.iface;

import org.json.JSONObject;

public interface ISerializableJson {
    void fromJson(JSONObject jsonObject);
    JSONObject toJson();
}
