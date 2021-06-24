package iface_global;

import org.json.JSONObject;

public interface ISerializableJson {
    void fromJson(JSONObject jsonObject, IErrCatch errCatch);
    JSONObject toJson();
}
