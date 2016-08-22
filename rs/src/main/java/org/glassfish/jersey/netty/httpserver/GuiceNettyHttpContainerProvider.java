package org.glassfish.jersey.netty.httpserver;

import com.catorv.gallop.rs.guice.bridge.api.GuiceBridge;
import com.catorv.gallop.rs.guice.bridge.api.GuiceIntoHK2Bridge;
import com.google.inject.Injector;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spi.ContainerProvider;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Application;
import java.net.URI;

/**
 * Created by cator on 8/21/16.
 */
@SuppressWarnings("unchecked")
public class GuiceNettyHttpContainerProvider implements ContainerProvider {
	public GuiceNettyHttpContainerProvider() {
	}

	public <T> T createContainer(Class<T> type, Application application) throws ProcessingException {
		return NettyHttpContainer.class == type ? type.cast(new NettyHttpContainer(application)) : null;
	}

	public static Channel createServer(URI baseUri, ResourceConfig configuration,
	                                   Injector injector, SslContext sslContext,
	                                   boolean block)
			throws ProcessingException {

		final NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
		final NioEventLoopGroup workerGroup = new NioEventLoopGroup();
		final NettyHttpContainer container = new NettyHttpContainer(configuration);

		resolve(injector, container.getApplicationHandler().getServiceLocator());

		try {
			ServerBootstrap e = new ServerBootstrap();
			e.option(ChannelOption.SO_BACKLOG, 1024);
			e.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new JerseyServerInitializer(baseUri, sslContext, container));
			final int port = getPort(baseUri);
			Channel ch = e.bind(port).sync().channel();
			ch.closeFuture().addListener(new GenericFutureListener() {
				public void operationComplete(Future future) throws Exception {
					container.getApplicationHandler().onShutdown(container);
					bossGroup.shutdownGracefully();
					workerGroup.shutdownGracefully();
				}
			});
			if (block) {
				ch.closeFuture().sync();
				return ch;
			} else {
				return ch;
			}
		} catch (InterruptedException var10) {
			throw new ProcessingException(var10);
		}
	}

	public static Channel createServer(URI baseUri, ResourceConfig configuration,
	                                   Injector injector, boolean block)
			throws ProcessingException {
		return createServer(baseUri, configuration, injector, null, block);
	}

	public static Channel createHttp2Server(URI baseUri,
	                                        ResourceConfig configuration,
	                                        Injector injector,
	                                        SslContext sslContext)
			throws ProcessingException {

		final NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
		final NioEventLoopGroup workerGroup = new NioEventLoopGroup();
		final NettyHttpContainer container = new NettyHttpContainer(configuration);

		resolve(injector, container.getApplicationHandler().getServiceLocator());

		try {
			ServerBootstrap e = new ServerBootstrap();
			e.option(ChannelOption.SO_BACKLOG, 1024);
			e.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new JerseyServerInitializer(baseUri, sslContext, container, true));
			final int port = getPort(baseUri);
			Channel ch = e.bind(port).sync().channel();
			ch.closeFuture().addListener(new GenericFutureListener() {
				public void operationComplete(Future future) throws Exception {
					container.getApplicationHandler().onShutdown(container);
					bossGroup.shutdownGracefully();
					workerGroup.shutdownGracefully();
				}
			});
			return ch;
		} catch (InterruptedException var9) {
			throw new ProcessingException(var9);
		}
	}

	private static void resolve(Injector injector, ServiceLocator locator) {
		GuiceBridge.getGuiceBridge().initializeGuiceBridge(locator);
		GuiceIntoHK2Bridge g2h = locator.getService(GuiceIntoHK2Bridge.class);
		g2h.bridgeGuiceInjector(injector);
	}

	private static int getPort(URI uri) {
		if (uri.getPort() == -1) {
			if ("http".equalsIgnoreCase(uri.getScheme())) {
				return 80;
			} else if ("https".equalsIgnoreCase(uri.getScheme())) {
				return 443;
			} else {
				throw new IllegalArgumentException("URI scheme must be \'http\' or \'https\'.");
			}
		} else {
			return uri.getPort();
		}
	}
}

