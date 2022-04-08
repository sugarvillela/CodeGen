package runstate;

import codedef.impl.AttribConvertUtil;
import codejson.JsonErrHandler;
import translators.iface.ITranslationCenter;
import translators.impl.TranslationCenter;
import codedef.codenode.NodeWrapUtil;
import util_enu.UtilEnum;
import utilfile.FileNameUtil;
import err.iface.IErr;
import err.impl.Err;
import err.impl.ErrDev;
import codedef.impl.PrototypeFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Glob {
    // Error settings
    public static final boolean             DISPLAY_DEV_ERRORS =    true;   // disable for release?
    public static final boolean             STACK_TRACE_ON_ERR =    true;   // disable for release?
    public static final int                 STACK_TRACE_DISP_N =    7;      // how many stack trace items to display
    public static final IErr                ERR_DEV =               ErrDev.initInstance();
    public static final IErr                ERR =                   Err.initInstance();

    public static final String              DEFAULT_PATH =          "src" + File.separator + "test" + File.separator + "resources";

    public static final FileNameUtil        FILE_NAME_UTIL =        FileNameUtil.initInstance();
    public static final UtilEnum            UTIL_ENUM =             UtilEnum.initInstance();
    public static final ITranslationCenter  TRANSLATION_CENTER =    TranslationCenter.initInstance();
    public static final JsonErrHandler      JSON_ERR_HANDLER =      JsonErrHandler.initInstance();
    public static final NodeWrapUtil CONVERT_UTIL =          NodeWrapUtil.initInstance();
    public static final AttribConvertUtil   ATTRIB_CONVERT_UTIL =   AttribConvertUtil.initInstance();
    public static final String              TIME_INIT =             (new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")).format(new Date());
    //laptop
    public static final String GEN_PATH = "C:\\Users\\daves\\OneDrive\\Documents\\GitHub\\SemanticAnalyzer\\src\\main\\java\\generated";
    public static final PrototypeFactory    PROTOTYPE_FACTORY =     PrototypeFactory.initInstance();
    //desktop
    //public static final String GEN_PATH = "C:\\Users\\Dave Swanson\\OneDrive\\Documents\\GitHub\\SemanticAnalyzer\\src\\main\\java\\generated";

}
