package fr.imcoding.edu365.persistence.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import fr.imcoding.edu365.enumeration.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//entité pour gérer les promotions
@Entity
@Table(name = "edu365_promotion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Promotion extends BaseEntity {

	private static final long serialVersionUID = -4853308925982410868L;

	@Enumerated(EnumType.STRING)
	protected TransactionType promotionType;
	private String promotionCode;
	private boolean valid;
	@ManyToOne(cascade = CascadeType.MERGE)
	private User concernedUser;

}