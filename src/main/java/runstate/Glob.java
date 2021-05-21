package runstate;

import datasink.impl.DataSink;
import tokenizer.iface.ITokenizer;
import tokenizer.impl.Tokenizer;
import util_enu.UtilEnum;
import utilfile.FileNameUtil;
import utilfile.SmallFileDump;
import err.iface.IErr;
import err.impl.Err;
import err.impl.ErrDev;
import langdef.util.EnumsByType;
import pushpoputil.impl.PushPopUtil;
import runstate.impl.RunState;
import codedef.impl.PrototypeFactory;
import utiljson.UtilJson;

public class Glob {
    // Error settings
    public static final boolean             DISPLAY_DEV_ERRORS =    true;   // disable for release?
    public static final boolean             STACK_TRACE_ON_ERR =    true;   // disable for release?
    public static final int                 STACK_TRACE_DISP_N =    7;      // how many stack trace items to display
    public static final IErr                ERR_DEV =               ErrDev.initInstance();
    public static final IErr                ERR =                   Err.initInstance();

    public static final String              NULL_TEXT =             "-";    // for csv strings

    public static final ITokenizer          TOKENIZER =             Tokenizer.initInstance();
    public static final ITokenizer          JSON_TOKENIZER =        Tokenizer.initJSONTokenizer();
    public static final FileNameUtil        FILE_NAME_UTIL =        FileNameUtil.initInstance();
    public static final SmallFileDump       SMALL_FILE_DUMP =       SmallFileDump.initInstance();

    public static final RunState            RUN_STATE =             RunState.initInstance();

    public static final EnumsByType         ENUMS_BY_TYPE =         EnumsByType.initInstance();
    public static final PushPopUtil         PUSH_POP_UTIL =         PushPopUtil.initInstance();
    public static final DataSink            DATA_SINK =             DataSink.initInstance();

    public static final UtilEnum            UTIL_ENUM =             UtilEnum.initInstance();
    public static final UtilJson            UTIL_JSON =             UtilJson.initInstance();
    public static final PrototypeFactory    PROTOTYPE_FACTORY =     PrototypeFactory.initInstance();
}
