package translators.iface;

import codedef.iface.ICodeNode;
import generictree.iface.IGTreeNode;
import langformat.iface.IFormatUtil;

import java.util.List;

public interface ITranslator {
    default void translate(IFormatUtil formatUtil, ICodeNode codeNode){
        this.buildHeader(formatUtil, codeNode);
        this.inc(formatUtil);
        this.recurse(formatUtil, codeNode);
        this.dec(formatUtil);
        this.buildFooter(formatUtil, codeNode);
    }

    default void buildHeader(IFormatUtil formatUtil, ICodeNode codeNode){}
    default void inc(IFormatUtil formatUtil){}

    default void recurse(IFormatUtil formatUtil, ICodeNode codeNode){
        IGTreeNode<ICodeNode> treeNode = codeNode.getParentTreeNode();
        if(treeNode == null){
            System.out.println(codeNode);
        }
        List<ICodeNode> payloadChildren = treeNode.getPayloadChildren();
        if(payloadChildren == null){
            System.out.println(treeNode.csvString());
        }
        for(ICodeNode childNode : payloadChildren){
            childNode.translator().translate(formatUtil, childNode);
        }
    }

    default void dec(IFormatUtil formatUtil){}
    default void buildFooter(IFormatUtil formatUtil, ICodeNode codeNode){}
}
