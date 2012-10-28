/**
 * Most of the code in the Qalingo project is copyrighted Hoteia and licensed
 * under the Apache License Version 2.0 (release version ${license.version})
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *                   Copyright (c) Hoteia, 2012-2013
 * http://www.hoteia.com - http://twitter.com/hoteia - contact@hoteia.com
 *
 */
package fr.hoteia.qalingo.web.handler.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.stereotype.Component;

import fr.hoteia.qalingo.core.common.domain.Localization;
import fr.hoteia.qalingo.core.common.domain.Market;
import fr.hoteia.qalingo.core.common.domain.MarketArea;
import fr.hoteia.qalingo.core.common.domain.MarketPlace;
import fr.hoteia.qalingo.core.common.domain.Retailer;
import fr.hoteia.qalingo.core.common.service.UrlService;
import fr.hoteia.qalingo.core.web.util.RequestUtil;

@Component(value="accessDeniedHandler")
public class ExtAccessDeniedHandlerImpl extends AccessDeniedHandlerImpl {

	private final Logger LOG = LoggerFactory.getLogger(getClass());

	@Autowired
    protected UrlService urlService;
	
	@Autowired
    protected RequestUtil requestUtil;
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
					   AccessDeniedException accessDeniedException) throws IOException, ServletException {
		try {
			final MarketPlace currentMarketPlace = requestUtil.getCurrentMarketPlace(request);
			final Market currentMarket = requestUtil.getCurrentMarket(request);
			final MarketArea currentMarketArea = requestUtil.getCurrentMarketArea(request);
			final Localization currentLocalization = requestUtil.getCurrentLocalization(request);
			final Retailer currentRetailer = requestUtil.getCurrentRetailer(request);
			String url = urlService.buildForbiddenUrl(request, currentMarketPlace, currentMarket, currentMarketArea, currentLocalization, currentRetailer);
			setErrorPage(url);
		} catch (Exception e) {
			LOG.error("", e);
		}
		super.handle(request, response, accessDeniedException);
	}
	
	
}
