package stackpayload.impl;

import langdefalgo.iface.LANG_STRUCT;

public class StackPayload extends StackPayloadBase {
    public StackPayload(LANG_STRUCT parentEnum) {
        super(parentEnum, new PayloadState());
    }
}
