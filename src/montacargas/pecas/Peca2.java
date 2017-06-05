package montacargas.pecas;

import montacargas.ActionLeft;
import montacargas.ActionRight;

public class Peca2 extends Peca {

    public Peca2(int i, int j) {
        super(i, j);
        tamanho = 1;
        
        actions.add(new ActionRight(this));
        actions.add(new ActionLeft(this));
    }
    
    @Override
    public Peca clone() {
        return new Peca2(linha, coluna);
    }
}