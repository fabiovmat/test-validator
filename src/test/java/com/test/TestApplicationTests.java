package com.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.passay.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class TestApplicationTests {

	//@Test
	//void contextLoads() {
	//}

	@Test
	public void ValidaComprimentodaSenha() throws IOException {

		//Teste regra 1
		Rule rule1 = new LengthRule(8, 16);
		PasswordValidator validator = new PasswordValidator(rule1);
		RuleResult tooShort = validator.validate(new PasswordData("XXXX"));
		RuleResult tooLong = validator.validate(new PasswordData("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ"));

		assertEquals("Password must be 8 or more characters in length.", validator.getMessages(tooShort)
				.get(0));
		assertEquals("Password must be no more than 16 characters in length.", validator.getMessages(tooLong)
				.get(0));
	}

	@Test
	public void ValidaEspacoEmBranco() throws IOException {

		Rule rule2 = new WhitespaceRule();
		PasswordValidator validator = new PasswordValidator(rule2);
		RuleResult whitespace = validator.validate(new PasswordData("aa aa aa "));
		assertEquals("Password contains a whitespace character.", validator.getMessages(whitespace).get(0));
	}

	@Test
	public boolean ValidacaodeUpperCase() throws IOException {


		CharacterCharacteristicsRule characterCharacteristicsRule = new CharacterCharacteristicsRule(
				3,
				new CharacterRule(EnglishCharacterData.LowerCase, 5),
				new CharacterRule(EnglishCharacterData.UpperCase, 5),
				new CharacterRule(EnglishCharacterData.Digit),
				new CharacterRule(EnglishCharacterData.Special)
		);

		PasswordValidator validator = new PasswordValidator(characterCharacteristicsRule);
		RuleResult result = validator.validate(new PasswordData("fdafafasfa"));


		if (result.isValid()) {
			System.out.println("Valid password");
			return true;
		}
		for (String m : validator.getMessages(result)) {
			System.err.println(m);
		}
		return false;



	}

	@Test
	public void ValidaRegex() throws IOException {

		Rule rule4 = new RepeatCharacterRegexRule(3);

		PasswordValidator validator = new PasswordValidator(rule4);
		RuleResult whitespace = validator.validate(new PasswordData("aaa "));
		assertEquals("Password matches the illegal pattern 'aaa'.", validator.getMessages(whitespace).get(0));
	}

	@Test
	public void ValidaRegexPattern() throws IOException{

		Rule rule5 = new AllowedRegexRule("!@#$%^&*()-+");
		PasswordValidator validator = new PasswordValidator(rule5);
		RuleResult whitespace = validator.validate(new PasswordData("aaaSAFD "));
		assertEquals("Password must match pattern '!@#$%^&*()-+'.", validator.getMessages(whitespace).get(0));
	}

	}





