package translators.genjava;

import codedef.iface.IAttribModifier;
import codedef.iface.ICodeNode;
import codedef.impl.PrototypeFactory;
import codedef.enums.CODE_NODE;
import langformat.iface.INullableUtil;
import langformat.impl.NullableUtil;
import runstate.Glob;
import translators.iface.IImportScraper;

import java.util.*;

import static codedef.enums.CODE_NODE.*;
import static codedef.enums.MODIFIER.LIT_;

public class ImportScraperJava implements IImportScraper {
    protected final Map<String, Set<CODE_NODE>> acc;
    protected final INullableUtil nullableUtil;
    protected final PrototypeFactory f;

    public ImportScraperJava() {
        acc = new HashMap<>();
        nullableUtil = NullableUtil.initInstance();
        f = Glob.PROTOTYPE_FACTORY;
    }

    @Override
    public void autoAddImports(ICodeNode root) {
        this.parseToGetImports(root, null, null);
        Map<String, List<String>> imports = this.getImports();
        this.parseToSetImports(root, null, imports);
    }

    private void parseToGetImports(ICodeNode codeNode, String currPathInfo, Set<CODE_NODE> currAcc) {
        List<ICodeNode> children = codeNode.getChildren();

        if(codeNode.havePathInfo()){    // depth-first now at the level of file
            currPathInfo = codeNode.getPathInfo();
            currAcc = new HashSet<>();
            for(ICodeNode childNode : children){
                this.parseToGetImports(childNode, currPathInfo, currAcc);
            }
            acc.put(currPathInfo, currAcc);
        }
        else {
            if(currAcc != null){       // below level of file
                CODE_NODE codeNodeEnum = codeNode.enumGroup();
                if(codeNodeEnum == VAR_DEF_GRP || codeNodeEnum == NEW_DEF_GRP){
                    currAcc.add(codeNode.getCodeNodeEnum());
                }
            }
            for(ICodeNode childNode : children){
                this.parseToGetImports(childNode, currPathInfo, currAcc);
            }
        }
    }
    private Map<String, List<String>> getImports() {
        Map<CODE_NODE, String> map = this.importsByEnum();
        Map<String, List<String>> out = new HashMap<>();

        for (Map.Entry<String, Set<CODE_NODE>> entry : this.acc.entrySet()){
            Set<String> currImports = new HashSet<>();

            Iterator<CODE_NODE> itr = entry.getValue().iterator();
            while(itr.hasNext()){
                CODE_NODE next = itr.next();
                String importText = map.get(next);
                if(importText != null){
                    currImports.add(importText);
                }
                List<String> imports = new ArrayList<>(currImports);
                Collections.sort(imports);
                out.put(entry.getKey(), this.toSortedList(currImports));
            }
        }
        return out;
    }
    private Map<CODE_NODE, String> importsByEnum(){
        Map<CODE_NODE, String> map = new HashMap<>();
        map.put(VAR_DEF_LIST,   "java.util.List");
        map.put(VAR_DEF_MAP,    "java.util.Map");
        map.put(VAR_DEF_SET,    "java.util.Set");
        map.put(NEW_LIST,       "java.util.ArrayList");
        map.put(NEW_MAP,        "java.util.HashMap");
        map.put(NEW_SET,        "java.util.HashSet");
        return map;
    }
    private List<String> toSortedList(Set<String> set){
        List<String> list = new ArrayList<>(set);
        Collections.sort(list);
        return list;
    }

    private void parseToSetImports(ICodeNode codeNode, String currPathInfo, Map<String, List<String>> imports){
        List<ICodeNode> children = codeNode.getChildren();

        if(codeNode.havePathInfo()){    // depth-first now at the level of file
            currPathInfo = codeNode.getPathInfo();
            for(ICodeNode childNode : children){
                this.parseToSetImports(childNode, currPathInfo, imports);
            }
        }
        else {
            if(currPathInfo != null && codeNode.enumGroup() == IMPORT){ // below level of file
                this.setImports(codeNode, imports.get(currPathInfo));
            }
            for(ICodeNode childNode : children){
                this.parseToSetImports(childNode, currPathInfo, imports);
            }
        }
    }

    private void setImports(ICodeNode codeNode, List<String> scrapedImportList){//code node is IMPORT
        if(scrapedImportList != null){
            List<ICodeNode> children = codeNode.getChildren();
            List<String> existingImports = getExistingImports(codeNode);
            for(String importText : scrapedImportList){
                if(!existingImports.contains(importText)){
                    children.add(f.get(IMPORT_ITEM).putAttrib(LIT_, importText));
                }
            }
        }
    }

    List<String> getExistingImports(ICodeNode codeNode){//code node is IMPORT
        List<String> existingImports = new ArrayList<>();
        for(ICodeNode importItem : codeNode.getChildren()){//child is IMPORT_ITEM
            IAttribModifier attributes = importItem.getAttribModifier();
            String value = nullableUtil.extractString(attributes.get(LIT_));
            if(value != null){
                existingImports.add(value);
            }
        }
        return existingImports;
    }
}
