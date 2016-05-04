package napwork;

abstract class Device {
	abstract void open(int param);
	abstract void close(int param);
	abstract void write(int param);
	abstract Object read(int param);
	abstract void setConfig(int param, Object value);
	abstract Object getConfig(int param);
}
