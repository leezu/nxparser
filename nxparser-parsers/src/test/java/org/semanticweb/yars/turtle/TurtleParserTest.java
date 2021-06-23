package org.semanticweb.yars.turtle;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import org.junit.Test;
import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Nodes;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.namespace.XSD;
import org.semanticweb.yars.utils.CallbackIterator;
/**
 * Turtle Parser test cases.
 *
 * @author Tobias Käfer
 */
public class TurtleParserTest {

	static Charset UTF_8 = Charset.forName("utf-8"); // StandardCharsets.UTF_8

	@Test
	public void relativeURIandRDFtypeAndMultilineTest()
			throws ParseException, URISyntaxException, InterruptedException, org.semanticweb.yars.nx.parser.ParseException {
		TurtleParser tp = new TurtleParser(new ByteArrayInputStream(("</a> a \"b\"^^<http://ex.org/> .\n"
				+ "</a> a \"b\"^^<http://ex.org/> .\n")
				.getBytes(UTF_8)), UTF_8, new URI("http://example.org/123/"));

		CallbackIterator it = new CallbackIterator();
		tp.parse(it);

		for (Node[] nx : it) {
			Nodes nodes = new Nodes(nx);
			assertEquals(
					nodes,
					new Nodes(
							new Node[] {
									new Resource("<http://example.org/a>", true),
									new Resource(
											"<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>",
											true),
									new Literal("\"b\"^^<http://ex.org/>", true) }));
		}

	}

	@Test
	public void decimalOrTripleEndTest() throws InterruptedException, org.semanticweb.yars.nx.parser.ParseException {
		
		TurtleParser tp = new TurtleParser(new ByteArrayInputStream(("<#a> <#value> 3.")
				.getBytes(UTF_8)), UTF_8, URI.create("http://example.org/"));
		CallbackIterator it = new CallbackIterator();
		tp.parse(it);
		
		Nodes nx = new Nodes(it.next());
		assertEquals(nx, new Nodes(
				new Node[] {
						new Resource("<http://example.org/#a>", true),
						new Resource("<http://example.org/#value>", true),
						new Literal("3",XSD.INTEGER)
				}));
	}

}
