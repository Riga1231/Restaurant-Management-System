module CaseStudy {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires java.desktop;
	requires java.sql;
	requires javafx.base;
	
	requires java.xml;
	requires AnimateFX;
	
	opens application to javafx.graphics, javafx.fxml, javafx.base;
}
