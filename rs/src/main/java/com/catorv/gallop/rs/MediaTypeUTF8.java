package com.catorv.gallop.rs;

import javax.ws.rs.core.MediaType;

/**
 * Media Type UTF8
 * Created by cator on 8/17/16.
 */
public class MediaTypeUTF8 extends MediaType {

	private static final String CHARSET = "utf-8";

	public static final String WILDCARD = MediaType.WILDCARD + "; " + MediaType.CHARSET_PARAMETER + "=" + CHARSET;
	public static final MediaType WILDCARD_TYPE = MediaType.WILDCARD_TYPE.withCharset(CHARSET);
	public static final String APPLICATION_XML = MediaType.APPLICATION_XML + "; " + MediaType.CHARSET_PARAMETER + "=" + CHARSET;
	public static final MediaType APPLICATION_XML_TYPE = MediaType.APPLICATION_XML_TYPE.withCharset(CHARSET);
	public static final String APPLICATION_ATOM_XML = MediaType.APPLICATION_ATOM_XML + "; " + MediaType.CHARSET_PARAMETER + "=" + CHARSET;
	public static final MediaType APPLICATION_ATOM_XML_TYPE = MediaType.APPLICATION_ATOM_XML_TYPE.withCharset(CHARSET);
	public static final String APPLICATION_XHTML_XML = MediaType.APPLICATION_XHTML_XML + "; " + MediaType.CHARSET_PARAMETER + "=" + CHARSET;
	public static final MediaType APPLICATION_XHTML_XML_TYPE = MediaType.APPLICATION_XHTML_XML_TYPE.withCharset(CHARSET);
	public static final String APPLICATION_SVG_XML = MediaType.APPLICATION_SVG_XML + "; " + MediaType.CHARSET_PARAMETER + "=" + CHARSET;
	public static final MediaType APPLICATION_SVG_XML_TYPE = MediaType.APPLICATION_SVG_XML_TYPE.withCharset(CHARSET);
	public static final String APPLICATION_JSON = MediaType.APPLICATION_JSON + "; " + MediaType.CHARSET_PARAMETER + "=" + CHARSET;
	public static final MediaType APPLICATION_JSON_TYPE = MediaType.APPLICATION_JSON_TYPE.withCharset(CHARSET);
	public static final String APPLICATION_FORM_URLENCODED = MediaType.APPLICATION_FORM_URLENCODED + "; " + MediaType.CHARSET_PARAMETER + "=" + CHARSET;
	public static final MediaType APPLICATION_FORM_URLENCODED_TYPE = MediaType.APPLICATION_FORM_URLENCODED_TYPE.withCharset(CHARSET);
	public static final String MULTIPART_FORM_DATA = MediaType.MULTIPART_FORM_DATA + "; " + MediaType.CHARSET_PARAMETER + "=" + CHARSET;
	public static final MediaType MULTIPART_FORM_DATA_TYPE = MediaType.MULTIPART_FORM_DATA_TYPE.withCharset(CHARSET);
	public static final String APPLICATION_OCTET_STREAM = MediaType.APPLICATION_OCTET_STREAM + "; " + MediaType.CHARSET_PARAMETER + "=" + CHARSET;
	public static final MediaType APPLICATION_OCTET_STREAM_TYPE = MediaType.APPLICATION_OCTET_STREAM_TYPE.withCharset(CHARSET);
	public static final String TEXT_PLAIN = MediaType.TEXT_PLAIN + "; " + MediaType.CHARSET_PARAMETER + "=" + CHARSET;
	public static final MediaType TEXT_PLAIN_TYPE = MediaType.TEXT_PLAIN_TYPE.withCharset(CHARSET);
	public static final String TEXT_XML = MediaType.TEXT_XML + "; " + MediaType.CHARSET_PARAMETER + "=" + CHARSET;
	public static final MediaType TEXT_XML_TYPE = MediaType.TEXT_XML_TYPE.withCharset(CHARSET);
	public static final String TEXT_HTML = MediaType.TEXT_HTML + "; " + MediaType.CHARSET_PARAMETER + "=" + CHARSET;
	public static final MediaType TEXT_HTML_TYPE = MediaType.TEXT_HTML_TYPE.withCharset(CHARSET);

}
