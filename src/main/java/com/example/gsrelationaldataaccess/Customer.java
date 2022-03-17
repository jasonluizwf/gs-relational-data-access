package com.example.gsrelationaldataaccess;


//A lógica de acesso a dados simples com a qual você trabalhará gerencia o nome e o sobrenome dos clientes.
public class Customer {
    private long id;
    private String firstName, lastName;

    public Customer(long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    /*  @Overrider é uma anotação que informa ao compilador que a intenção seria de sobrescrever.
        Dessa forma, o compilador pode avaliar se a assinatura está coerente com algum método das
        superclasses e emite um aviso caso o método não esteja realmente sobrescrevendo algo!
     */
    @Override
    public String toString() {
        return String.format(
                "[id='%d', firstName='%s', lastName='%s']",
        id, firstName, lastName);
    }
}
