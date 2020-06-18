package clueGame;

/**
 * Card - class with the attributes of a card
 * @author sethasadi dquintana
 *
 */
public class Card {
	private CardType cardType;
	private String cardName;
	
	public Card(String name, CardType type) {
		cardName = name;
		cardType = type;
	}
	
	public Card() {
		
	}
	
	public CardType getCardType() {
		return cardType;
	}
	
	public String getCardName() {
		return cardName;
	}
	
	public boolean equals(String toCompare) {
		//return true if toCompare has the same same as the cardName
		return cardName.equals(toCompare);
	}

	public void setCardType(CardType cardType) {
		this.cardType = cardType;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	
	
}
