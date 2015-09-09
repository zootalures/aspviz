package edu.bath.aspviz.sg;
import java.util.ArrayList;
import java.util.List;



/** 
 * Three D node
 * @author occ
 *
 */
public class Node<Context>{
	List<Node> children = new ArrayList<Node>();
	String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<Node> getChildren() {
		return children;
	}
	public void setChildren(List<Node> children) {
		this.children = children;
	}
	public List<ContextAction<Context>> getTransforms() {
		return transforms;
	}
	public void setTransforms(List<ContextAction<Context>> transform) {
		this.transforms = transform;
	}
	public Node<Context> addTransform(ContextAction<Context> transform){
		this.transforms.add(transform);
		return this;
	}
	
	public List<ContextAction> getShapes() {
		return shapes;
	}
	
	public Node<Context> addShape(ContextAction<Context> shape){
		shapes.add(shape);
		return this;
	}
	
	public Node<Context> addAttribute(ContextAction<Context> attribute){
		attributes.add(attribute);
		return this;
	}
	
	public Node<Context> addChild(Node<Context> child){
		children.add(child);
		return this;
	}
	
	public void setShapes(List<ContextAction> shapes) {
		this.shapes = shapes;
	}
	List<ContextAction<Context>> attributes = new ArrayList<ContextAction<Context>>();
	
	public List<ContextAction<Context>> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<ContextAction<Context>> attributes) {
		this.attributes = attributes;
	}
	List<ContextAction<Context>> transforms = new ArrayList<ContextAction<Context>>();
	List<ContextAction> shapes = new ArrayList<ContextAction>();
	
}