package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.ConvFloat;
import fr.ensimag.deca.tree.ListInst;
import fr.ensimag.deca.tree.Minus;
import fr.ensimag.deca.tree.Plus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;


// ON HOLD 

public class TestIfElse {
    final Type BOOLEAN = new BooleanType(null);
    final Type INT = new IntType(null);

    @Mock
    AbstractExpr cond1;
    @Mock
    ListInst thenbranch1;
    @Mock
    ListInst elsebranch1;

    DecacCompiler compiler;

    @BeforeEach
    public void setup() throws ContextualError{
        MockitoAnnotations.initMocks(this);
        compiler= new DecacCompiler(null, null);
        when(cond1.verifyExpr(compiler, null, null)).thenReturn(BOOLEAN);
    }
}
