package drgMB;

public class MapTile extends HexTile {
	private int angle;
	private SegmentTile segmentTile;
	private Mineral mineralContent;
	
	public MapTile() {
		this.angle = 0;
		this.segmentTile = null;
		this.mineralContent = null;
	}
	
	public MapTile getAdjacentTile(int position) {
		return (MapTile)super.getAdjacentTile(position);
	}
	
	public MapTile getAdjacentTile(HexTile.AdjacentTile position) {
		return (MapTile)super.getAdjacentTile(position);
	}
	
	public int getAngle() {
		return this.angle;
	}
	
	public SegmentTile getSegmentTile() {
		return this.segmentTile;
	}
	
	public void setSegmentTile(SegmentTile segmentTile) {
		this.segmentTile = segmentTile;
	}
	
	public Mineral getMineral() {
		return this.mineralContent;
	}
	
	public void setMineral(Mineral mineralContent) {
		this.mineralContent = mineralContent;
	}
}
