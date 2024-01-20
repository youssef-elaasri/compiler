parser grammar DecaParser;

options {
    // Default language but name it anyway
    //
    language  = Java;

    // Use a superclass to implement all helper
    // methods, instance variables and overrides
    // of ANTLR default methods, such as error
    // handling.
    //
    superClass = AbstractDecaParser;

    // Use the vocabulary generated by the accompanying
    // lexer. Maven knows how to work out the relationship
    // between the lexer and parser and will build the
    // lexer before the parser. It will also rebuild the
    // parser if the lexer changes.
    //
    tokenVocab = DecaLexer;

}

// which packages should be imported?
@header {
    import fr.ensimag.deca.tree.*;
    import java.io.PrintStream;
    import fr.ensimag.deca.tools.SymbolTable;
}

@members {
    @Override
    protected AbstractProgram parseProgram() {
        return prog().tree;
    }
    SymbolTable symbolTable = new SymbolTable();
}

prog returns[AbstractProgram tree]
    : list_classes main EOF {
            assert($list_classes.tree != null);
            assert($main.tree != null);
            $tree = new Program($list_classes.tree, $main.tree);
            setLocation($tree, $list_classes.start);
        }
    ;

main returns[AbstractMain tree]
    : /* epsilon */ {
            $tree = new EmptyMain();
        }
    | block {
            assert($block.decls != null);
            assert($block.insts != null);
            $tree = new Main($block.decls, $block.insts);
            setLocation($tree, $block.start);
        }
    ;

block returns[ListDeclVar decls, ListInst insts]
    : OBRACE list_decl list_inst CBRACE {
            assert($list_decl.tree != null);
            assert($list_inst.tree != null);
            $decls = $list_decl.tree;
            $insts = $list_inst.tree;
        }
    ;

list_decl returns[ListDeclVar tree]
@init   {
            $tree = new ListDeclVar();
        }
    : decl_var_set[$tree]*
    ;

decl_var_set[ListDeclVar l]
    : type list_decl_var[$l,$type.tree] SEMI
    ;

list_decl_var[ListDeclVar l, AbstractIdentifier t]
    : dv1=decl_var[$t] {
        assert($dv1.tree != null);
        $l.add($dv1.tree);
        } (COMMA dv2=decl_var[$t] {
            assert($dv2.tree != null);
            $l.add($dv2.tree);
        }
      )*
    ;

decl_var[AbstractIdentifier t] returns[AbstractDeclVar tree]
@init   {
            NoInitialization noInitialization = new NoInitialization();
        }
    : i=ident {
        $tree = new DeclVar($t,$i.tree,noInitialization);
        setLocation($tree, $i.start);
        }
      (EQUALS e=expr {
        Initialization initialization = new Initialization($e.tree);
        $tree = new DeclVar($t,$i.tree,initialization);
        setLocation($tree, $e.start);
        setLocation(initialization, $e.start);
        }
      )? {
        }
    ;

list_inst returns[ListInst tree]
@init {
        $tree = new ListInst();
}
    : (e=inst {
            assert($e.tree != null);
            $tree.add($e.tree);
        }
      )*
    ;

inst returns[AbstractInst tree]
    : e1=expr SEMI {
            assert($e1.tree != null);
            $tree = $e1.tree;
            setLocation($tree, $e1.start);
        }
    | SEMI {
        }
    | PRINT OPARENT list_expr CPARENT SEMI {
            assert($list_expr.tree != null);
            $tree = new Print(false,$list_expr.tree);
            setLocation($tree, $list_expr.start);
        }
    | PRINTLN OPARENT list_expr CPARENT SEMI {
            assert($list_expr.tree != null);
            $tree = new Println(false,$list_expr.tree);
            setLocation($tree, $list_expr.start);
        }
    | PRINTX OPARENT list_expr CPARENT SEMI {
            assert($list_expr.tree != null);
            $tree = new Print(true,$list_expr.tree);
            setLocation($tree, $list_expr.start);
        }
    | PRINTLNX OPARENT list_expr CPARENT SEMI {
            assert($list_expr.tree != null);
            $tree = new Println(true,$list_expr.tree);
            setLocation($tree, $list_expr.start);
        }
    | if_then_else {
            assert($if_then_else.tree != null);
            $tree = $if_then_else.tree;
            setLocation($tree, $if_then_else.start);
        }
    | WHILE OPARENT condition=expr CPARENT OBRACE body=list_inst CBRACE {
            assert($condition.tree != null);
            assert($body.tree != null);
            $tree = new While($condition.tree,$body.tree);
            setLocation($tree, $WHILE);
        }
    | RETURN e=expr SEMI {
            assert($e.tree != null);
            $tree= new Return($e.tree);
            setLocation($tree, $RETURN);
        }
    ;

 

if_then_else returns[IfThenElse tree]
@init {
        ListInst li_else_2 = new ListInst();
        ListInst current = new ListInst();
}
    : if1=IF OPARENT condition=expr CPARENT OBRACE li_if=list_inst CBRACE {
                assert($condition.tree != null);
                assert($li_if.tree != null);
                $tree =  new IfThenElse($condition.tree,$li_if.tree,li_else_2);
                current = li_else_2;
                setLocation($tree, $condition.start);
        }
      (ELSE elsif=IF OPARENT elsif_cond=expr CPARENT OBRACE elsif_li=list_inst CBRACE {
                assert($elsif_cond.tree != null);
                assert($elsif_li.tree != null);
                ListInst li_else_fin = new ListInst();
                IfThenElse if_then_else = new IfThenElse($elsif_cond.tree, $elsif_li.tree, li_else_fin);
                current.add(if_then_else);
                setLocation($tree, $elsif);
                setLocation(if_then_else, $elsif);
                current = li_else_fin;
        }
      )*
      (ELSE OBRACE li_else=list_inst CBRACE {
                assert($li_else.tree != null);
                Iterator<AbstractInst> iter = $li_else.tree.iterator();
                while (iter.hasNext()) {
                    current.add(iter.next());
                }
                setLocation($tree, $ELSE);
        }
      )?
    ;

list_expr returns[ListExpr tree]
@init   {
            $tree = new ListExpr();
        }
    : (e1=expr {
            assert($e1.tree != null);
            $tree.add($e1.tree);
            setLocation($tree, $e1.start);
        }
       (COMMA e2=expr {
            assert($e2.tree != null);
            $tree.add($e2.tree);
            setLocation($tree, $e2.start);
        }
       )* )?
    ;

expr returns[AbstractExpr tree]
    : e=assign_expr {
            assert($assign_expr.tree != null);
            $tree = $assign_expr.tree;
            setLocation($tree, $e.start);
        }
    ;

assign_expr returns[AbstractExpr tree]
    : e=or_expr (
        /* condition: expression e must be a "LVALUE" */ {
            if (! ($e.tree instanceof AbstractLValue)) {
                throw new InvalidLValue(this, $ctx);
            }
            $tree = $e.tree;
            setLocation($tree, $e.start);
        }
        EQUALS e2=assign_expr {
            assert($e.tree != null);
            assert($e2.tree != null);
            $tree = new Assign((AbstractLValue) $e.tree,$e2.tree);
            setLocation($tree, $e2.start);
        }
      | /* epsilon */ {
            assert($e.tree != null);
            $tree = $e.tree;
            setLocation($tree, $e.start);

        }
      )
    ;

or_expr returns[AbstractExpr tree]
    : e=and_expr {
            assert($e.tree != null);
            $tree = $e.tree;
            setLocation($tree, $e.start);
        }
    | e1=or_expr OR e2=and_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Or($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
       }
    ;

and_expr returns[AbstractExpr tree]
    : e=eq_neq_expr {
            assert($e.tree != null);
            $tree = $e.tree;
            setLocation($tree, $e.start);
        }
    |  e1=and_expr AND e2=eq_neq_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new And($e1.tree,$e2.tree);
            setLocation($tree, $e1.start);
        }
    ;

eq_neq_expr returns[AbstractExpr tree]
    : e=inequality_expr {
            assert($e.tree != null);
            $tree=$e.tree;
            setLocation($tree, $e.start);
        }
    | e1=eq_neq_expr EQEQ e2=inequality_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Equals($e1.tree,$e2.tree);
            setLocation($tree, $e1.start);
        }
    | e1=eq_neq_expr NEQ e2=inequality_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new NotEquals($e1.tree,$e2.tree);
            setLocation($tree, $e1.start);
        }
    ;

inequality_expr returns[AbstractExpr tree]
    : e=sum_expr {
            assert($e.tree != null);
            $tree=$e.tree;
            setLocation($tree, $e.start);
        }
    | e1=inequality_expr LEQ e2=sum_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new LowerOrEqual($e1.tree,$e2.tree);
            setLocation($tree, $e1.start);
        }
    | e1=inequality_expr GEQ e2=sum_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new GreaterOrEqual($e1.tree,$e2.tree);
            setLocation($tree, $e1.start);
        }
    | e1=inequality_expr GT e2=sum_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Greater($e1.tree,$e2.tree);
            setLocation($tree, $e1.start);
        }
    | e1=inequality_expr LT e2=sum_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Lower($e1.tree,$e2.tree);
            setLocation($tree, $e1.start);
        }
    | e1=inequality_expr INSTANCEOF type {
            assert($e1.tree != null);
            assert($type.tree != null);
        }
    ;


sum_expr returns[AbstractExpr tree]
    : e=mult_expr {
            assert($e.tree != null);
            $tree=$e.tree;
            setLocation($tree, $e.start);
        }
    | e1=sum_expr PLUS e2=mult_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Plus($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
    | e1=sum_expr MINUS e2=mult_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Minus($e1.tree, $e2.tree);
            setLocation($tree, $e1.start);
        }
    ;

mult_expr returns[AbstractExpr tree]
    : e=unary_expr {
            assert($e.tree != null);
            $tree=$e.tree;
            setLocation($tree, $e.start);
        }
    | e1=mult_expr TIMES e2=unary_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Multiply($e1.tree,$e2.tree);
            setLocation($tree,$e1.start);
        }
    | e1=mult_expr SLASH e2=unary_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Divide($e1.tree, $e2.tree);
            setLocation($tree,$e1.start);
        }
    | e1=mult_expr PERCENT e2=unary_expr {
            assert($e1.tree != null);
            assert($e2.tree != null);
            $tree = new Modulo($e1.tree, $e2.tree);
            setLocation($tree,$e1.start);
        }
    ;

unary_expr returns[AbstractExpr tree]
    : op=MINUS e=unary_expr {
            assert($e.tree != null);
            $tree = new UnaryMinus($e.tree);
            setLocation($tree, $e.start);
        }
    | op=EXCLAM e=unary_expr {
            assert($e.tree != null);
            $tree = new Not($e.tree);
            setLocation($tree, $e.start);
        }
    | e1=select_expr {
            assert($e1.tree != null);
            $tree=$e1.tree;
            setLocation($tree, $e1.start);
        }
    ;

select_expr returns[AbstractExpr tree]
    : e=primary_expr {
            assert($e.tree != null);
            $tree=$e.tree;
            setLocation($tree, $e.start);
        }
    | e1=select_expr DOT i=ident {
            assert($e1.tree != null);
            assert($i.tree != null);
        }
        (o=OPARENT args=list_expr CPARENT {
            // we matched "e1.i(args)"
            assert($args.tree != null);
            $tree = new MethodCall($e1.tree, $i.tree, $args.tree);
            setLocation($tree, $i.start);
        }
        | /* epsilon */ {
            // we matched "e.i"
            $tree = new Selection($e1.tree, $i.tree);
            setLocation($tree, $i.start);

        }
        )
    ;

primary_expr returns[AbstractExpr tree]
    : ident {
            assert($ident.tree != null);
            $tree = $ident.tree;
            setLocation($tree, $ident.start);
        }
    | m=ident OPARENT args=list_expr CPARENT {
            assert($args.tree != null);
            assert($m.tree != null);
        }
    | OPARENT expr CPARENT {
            assert($expr.tree != null);
            $tree = $expr.tree;
            setLocation($tree, $expr.start);
        }
    | READINT OPARENT CPARENT {
            $tree = new ReadInt();
        }
    | READFLOAT OPARENT CPARENT {
            $tree = new ReadFloat();
        }
    | NEW ident OPARENT CPARENT {
            assert($ident.tree != null);
            $tree = new New($ident.tree);
            setLocation($tree, $ident.start);
        }
    | cast=OPARENT type CPARENT OPARENT expr CPARENT {
            assert($type.tree != null);
            assert($expr.tree != null);
        }
    | e=literal {
            assert($literal.tree != null);
            $tree = $literal.tree;
            setLocation($tree, $literal.start);
        }
    ;

type returns[AbstractIdentifier tree]
    : ident {
            assert($ident.tree != null);
            $tree = $ident.tree;
            setLocation($tree,$ident.start);
        }
    ;

literal returns[AbstractExpr tree]
    : INT {
            try {
                $tree = new IntLiteral(Integer.parseInt($INT.text));
                setLocation($tree, $INT);
            }
            catch (NumberFormatException e) {
                throw new DecaRecognitionException("the integer may be too long or invalid",this,$ctx);
            }
        }
    | fd=FLOAT {
                  try {
                      $tree = new FloatLiteral(Float.parseFloat($fd.text));
                      setLocation($tree, $fd);
                      }
                  catch (Exception e) {
                      throw new DecaRecognitionException("Invalid float",this,$ctx);
                  }
              }
    | s=STRING {
            $tree = new StringLiteral($s.text);
            setLocation($tree, $s);
        }
    | t=TRUE {
            $tree = new BooleanLiteral(Boolean.parseBoolean($t.text));
            setLocation($tree, $t);
        }
    | f=FALSE {
            $tree = new BooleanLiteral(Boolean.parseBoolean($f.text));
            setLocation($tree, $f);
        }
    | THIS {
            $tree = new This();
        }
    | NULL {
        }
    ;

ident returns[AbstractIdentifier tree]
    : IDENT {

            $tree = new Identifier(symbolTable.create($IDENT.text));
            setLocation($tree, $IDENT);
        }
    ;

/****     Class related rules     ****/

list_classes returns[ListDeclClass tree]
    @init { 
         $tree = new ListDeclClass();
    }
    :  (c1=class_decl {
        $tree.add($c1.tree);
        }
      )*
    ;

class_decl returns[AbstractDeclClass tree]
    : CLASS name=ident superclass=class_extension OBRACE class_body CBRACE {
        assert($class_body.list_field != null);
        assert($class_body.list_method != null);
        $tree = new DeclClass($name.tree, $superclass.tree, $class_body.list_field, $class_body.list_method);
        setLocation($tree, $CLASS);
        }
    ;

class_extension returns[AbstractIdentifier tree]
    : EXTENDS ident {
        assert($ident.tree != null);
        $tree = $ident.tree;
        setLocation($tree, $ident.start);
        }
    | /* epsilon */ {
        $tree = new Identifier(symbolTable.create("Object"));
        setLocation($tree);
        }
    ;

class_body returns[ListDeclField list_field, ListDeclMethod list_method]
    @init {
        $list_field = new ListDeclField();
        $list_method = new ListDeclMethod();
    }
    : (m=decl_method {
           $list_method.add($m.tree);
        }
      | f=decl_field_set[$list_field] {
      }
      )*
    ;

decl_field_set[ListDeclField list_field]
    : v=visibility t=type list_decl_field[$list_field, $t.tree, $v.enum]
    SEMI {
    }
    ;

visibility returns[Visibility enum]
    : /* epsilon */ {
        $enum = Visibility.PUBLIC;
        }
    | pr=PROTECTED {
        $enum = Visibility.PROTECTED;
        }
    ;

list_decl_field[ListDeclField list_field, AbstractIdentifier t, Visibility v]
    : dv1=decl_field[$t, $v] {
        assert($dv1.tree != null);
        $list_field.add($dv1.tree);
    }
        (COMMA dv2=decl_field[$t, $v] {
            assert($dv2.tree != null);
            $list_field.add($dv2.tree);
        }
      )*
    ;

decl_field[AbstractIdentifier t, Visibility v] returns[AbstractDeclField tree]
@init {
    NoInitialization noInitialization = new NoInitialization();
}
    : i=ident {
        $tree = new DeclField($v ,$t, $i.tree, noInitialization);
        setLocation($tree, $i.start);
        }
      (EQUALS e=expr {
        Initialization initialization = new Initialization($e.tree);
        $tree = new DeclField($v, $t,$i.tree,initialization);
        setLocation($tree, $e.start);
        setLocation(initialization, $e.start);
        }
      )? {
        }
    ;

decl_method returns[AbstractDeclMethod tree]
@init {
    ListDeclParam list_param = new ListDeclParam();
    MethodBody methodBody;
}
    : type i=ident OPARENT params=list_params[list_param] CPARENT (b=block {
        methodBody=new MethodBody($block.decls,$block.insts);
        $tree = new DeclMethod($type.tree, $ident.tree, list_param,methodBody);
        setLocation($tree,$i.start);
        setLocation(methodBody,$i.start);
        }
      | ASM OPARENT code=multi_line_string CPARENT SEMI {
        }
      ) {
        }
    ;

list_params[ListDeclParam list_param]
    : (p1=param {
        assert($p1.tree != null);
        $list_param.add($p1.tree);
        } (COMMA p2=param {
        assert($p2.tree != null);
        $list_param.add($p2.tree);
        }
      )*)?
    ;
    
multi_line_string returns[String text, Location location]
    : s=STRING {
            $text = $s.text;
            $location = tokenLocation($s);
        }
    | s=MULTI_LINE_STRING {
            $text = $s.text;
            $location = tokenLocation($s);
        }
    ;

param returns[AbstractDeclParam tree]
    : t=type i=ident {
        assert($t.tree != null);
        assert($i.tree != null);
        $tree = new DeclParam($t.tree, $i.tree);
        }
    ;
