package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;

public class ListDeclField extends TreeList<AbstractDeclField>{
    @Override
    public void decompile(IndentPrintStream s) {
        for(AbstractDeclField f: getList()){
            f.decompile(s);
            s.println();
        }
    }

    public void verifyListDeclField(DecacCompiler compiler, AbstractIdentifier superId, AbstractIdentifier classId) throws ContextualError{
        for(AbstractDeclField field : this.getList()){
            field.verifyField(compiler, superId, classId);
        }
    }


    public void verifyListDeclFieldI(DecacCompiler compiler, AbstractIdentifier superId, AbstractIdentifier classId){
        throw new UnsupportedOperationException("not yet implemented");
    }


}
