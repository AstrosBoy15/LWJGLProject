package renderEngine;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class DisplayManager {
	
	private static long window;
	
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;

	public static void createDisplay() {
		
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		GLFW.glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		GLFW.glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		GLFW.glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
		GLFW.glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		GLFW.glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);

		// Create the window
		window = glfwCreateWindow(WIDTH, HEIGHT, "LWJGL Project", NULL, NULL);
		
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
		});
		
		// Get the thread stack and push a new frame
				try ( MemoryStack stack = stackPush() ) {
					IntBuffer pWidth = stack.mallocInt(1); // int*
					IntBuffer pHeight = stack.mallocInt(1); // int*

					// Get the window size passed to glfwCreateWindow
					glfwGetWindowSize(window, pWidth, pHeight);

					// Get the resolution of the primary monitor
					GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

					// Center the window
					glfwSetWindowPos(
						window,
						(vidmode.width() - pWidth.get(0)) / 2,
						(vidmode.height() - pHeight.get(0)) / 2
					);
				} // the stack frame is popped automatically

				// Make the OpenGL context current
				glfwMakeContextCurrent(window);
				// Enable v-sync
				glfwSwapInterval(1);

				// Make the window visible
				glfwShowWindow(window);
			}

	
	public static void updateDisplay() {

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		glfwSwapBuffers(window); // swap the color buffers

		// Poll for window events. The key callback above will only be
		// invoked during this call.
		glfwPollEvents();
		
	}
	
	public static void closeDisplay() {
		
		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	public static long getWindow() {
		return window;
	}
	
	public static int getWidth() {
		return WIDTH;
	}
	
	public static int getHeight() {
		return HEIGHT;
	}
	
}
