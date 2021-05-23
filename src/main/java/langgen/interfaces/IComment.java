package langgen.interfaces;

import langformat.impl.FormatUtil;

public interface IComment extends IWidget{
    IComment add(String... text);
    IComment finish(FormatUtil formatUtil);

    interface ICommentBuilder{
        ICommentBuilder setLong();
        IComment build();
    }
}
