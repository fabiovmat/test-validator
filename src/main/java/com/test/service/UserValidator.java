package com.test.service;

import org.passay.AllowedRegexRule;
import org.passay.CharacterCharacteristicsRule;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RepeatCharacterRegexRule;
import org.passay.Rule;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.test.model.User;

/*Classe responsavel por implementar o Validator(passay.org) que é a engine responsavel por fazer as validacoes de regras*/

@Component
public class UserValidator implements Validator {

	

	@Autowired
	private UserService userService;

	@Override
	public boolean supports(Class<?> aClass) {
		return User.class.equals(aClass);
	}

	@Override
	public void validate(Object o, Errors errors) {
		User user = (User) o;

		// Regra 1 Password deve ter entre 8 e 16 caracteres
		Rule rule1 = new LengthRule(8, 16);

		// Regra 2: Não é permitido espaço em branco
		Rule rule2 = new WhitespaceRule();
		CharacterCharacteristicsRule rule3 = new CharacterCharacteristicsRule();
		// numero de caracterisiticas da regra que devem ser satisfeitas
		rule3.setNumberOfCharacteristics(3);
		// Regra 3.a: Ao menos um caractere Upper Case
		rule3.getRules().add(new CharacterRule(EnglishCharacterData.UpperCase, 1));
		// Regra 3.b: Ao menos um caractere Lower-case
		rule3.getRules().add(new CharacterRule(EnglishCharacterData.LowerCase, 1));
		// Regra 3.c: Um digito obrigatório
		rule3.getRules().add(new CharacterRule(EnglishCharacterData.Digit, 1));
		// Regra 3.d: Um caractere especial obrigatório
		rule3.getRules().add(new CharacterRule(EnglishCharacterData.Special, 1));

		// Regra 4: Senha nao pode ter mais que 2 caracteres repetidos
		Rule rule4 = new RepeatCharacterRegexRule(3);
		// Regra 5 :Caracteres especiais permitidos
		Rule rule5 = new AllowedRegexRule("!@#$%^&*()-+");

		PasswordValidator validator = new PasswordValidator(rule1, rule2, rule3, rule4, rule5);
		PasswordData password = new PasswordData(user.getPassword());
		RuleResult result = validator.validate(password);

		if (result.isValid()) {
			System.out.println("Senha valida");
		} else {
			System.out.println("Senha invalida: " + validator.getMessages(result));

		}
		
		
		

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty");
		if (user.getUsername().length() < 9 || user.getUsername().length() > 32) {
			errors.rejectValue("username", "Size.userForm.username");
		}
		if (userService.findByUsername(user.getUsername()) != null) {
			errors.rejectValue("username", "Duplicate.userForm.username");
		}



	}

}
