/**
 * Base prototype for application
 * @example
 * var MyApp = function(){
 *   // calling super (parent class) constructor
 *   this.super_.apply(this, arguments);
 * }
 *  utils.inherits(MyApp, SimpleAppProto);
 *
 *  // Override super function
 *  MyApp.prototype.changeEditMode = function(){
 *    // Call the original method from super (parent)
 *    this.super_.changeEditMode.apply(this, arguments);
 *  }
 *  var App = new YourApp;
 * @constructor
 */

function SimpleAppProto(el){
	var self = this;
	self.editMode = Wix.Utils.getViewMode();
	self.deviceType = Wix.Utils.getDeviceType ? Wix.Utils.getDeviceType() : 'desktop';
	self.isMobile = (self.deviceType === 'mobile')
	self.el = el;

	if (!_.isFunction(self.updateSettings)) self.updateSettings = function(){}
	Wix.addEventListener(Wix.Events.SETTINGS_UPDATED, _.bind(self.handleSettingsEvent, self));
	Wix.addEventListener(Wix.Events.EDIT_MODE_CHANGE, _.bind(self.handleEditModeEvent, self));

	// Node: workaround for incorrect editMode reporting on initial SDK load in Editor
	Wix.getSiteInfo(function(siteInfo){
		if (siteInfo && siteInfo.url && /editor.wix/.test(siteInfo.url)&& !/feedback=true/.test(siteInfo.url)){
			self.handleEditModeEvent({editMode: 'editor'});
		} else {
			self.handleEditModeEvent({editMode: self.editMode});
		}
	});

	if (_.isFunction(self.destroy)) Wix.addEventListener(Wix.Events.COMPONENT_DELETED, _.bind(self.destroy, self));
	if (_.isFunction(self.navigate)) Wix.addEventListener(Wix.Events.PAGE_NAVIGATION_CHANGE, _.bind(self.navigate, self));
	if (_.isFunction(self.publish)) Wix.addEventListener(Wix.Events.SITE_PUBLISHED, _.bind(self.publish, self));

	if (_.isFunction(self.resize))
        $(window).on('resize', _.bind(self.resize, self));

    Wix.pushState(JSON.stringify({cmd:'componentReady', id: Wix.Utils.getCompId()}));
}

SimpleAppProto.prototype.handleSettingsEvent = function(message){
	var self = this;
	if (message.cmd){
		if (_.isFunction(self[message.cmd])) self[message.cmd].call(self, message);
	} else {
		self.updateSettings(message);
	}
}

SimpleAppProto.prototype.handleEditModeEvent = function(mode){
	var self = this;
	self.editMode = mode.editMode;
	var viewport = document.getElementById('viewport');
	if (viewport){
		viewport.setAttribute('data-mode', self.editMode);
	}
	if (_.isFunction(self.changeEditMode))
		self.changeEditMode(mode);
}

SimpleAppProto.prototype.openLink = function(href, target, linkType, e,anchorData,mainPageId, renderedLink){
	if (!href) return;
	var self = this

	switch (linkType){
		case 'PAGE':
			Wix.navigateToPage(renderedLink && renderedLink.pageId && renderedLink.pageId.id);
			break;
        case 'ANCHOR':
            if (anchorData === "SCROLL_TO_TOP" || anchorData === "SCROLL_TO_BOTTOM") {
                Wix.getCurrentPageId(function (currentPageId) {
                    Wix.pushState(JSON.stringify({cmd: 'navigateToAnchor', args: [currentPageId, anchorData]}));
                });
            } else {
                var pageId = renderedLink && renderedLink.pageId && renderedLink.pageId.id;
                Wix.pushState(JSON.stringify({cmd: 'navigateToAnchor', args: [pageId, anchorData]}));
            }
            break;
        case 'DYNAMIC_PAGE_LINK':
                Wix.pushState(JSON.stringify({cmd: 'navigateToDynamicPage', args:[renderedLink,anchorData,href]}));
                break;
		case 'WEBSITE':
			if(!target) {
				target = '_blank';
			} else if(target == '_self') {
				target = '_top';
			}
			if (this.editMode == 'preview' && target == '_top'){
				// TODO: in preview mode -- show message instead of navigating out of the editor
				var message = 'Your link will not open in the same window while in Preview mode, but it will work on your site!';
				var flyout = $('<div></div>').append(message).css({
					letterSpacing: '1px',
					boxShadow: 'rgba(0, 0, 0, 0.6) 0px 1px 4px 0px',
					borderRadius: '5px',
					maxWidth: '300px',
					color: 'rgb(101, 101, 101)',
					background: 'rgb(255, 254, 223)',
					position: 'absolute',
					padding: '5px',
					fontSize: '12px',
					fontFamily: 'Helvetica, Arial, serif',
					lineHeight: '16px'
				});
				var flyoutPos = {left: e.pageX, top: e.pageY}

				if (e.pageX && e.pageY){
					var containerWidth = $('body').width();
					flyoutPos.left -= 150;
					if (flyoutPos.left < 1) flyoutPos.left = 1
					if (flyoutPos.top < 1) flyoutPos.top = 1
					if (flyoutPos.left +300 > containerWidth) flyoutPos = containerWidth - 300;

				} else if(e.currentTarget){
					flyoutPos = $(e.currentTarget).offset()
				} else {
					flyoutPos = {left: 10, top: 10};
				}
				var overlay = self.viewportOverlay().show(flyout, 5000, flyoutPos);
			} else {
				window.open(href, target);
			}
			break;
		case 'EMAIL':
			window.open(href);
			break;
		case 'DOCUMENT':
			window.open(href, '_blank');
			break;
	}
}

function SimpleAppOverlay(el){
	var self = this;
	self.overlay = $('<div></div>').appendTo('body');
	self.el = el;
	self.hide();
	self.overlay.on('click', function(){
		self.hide();
	});
	return self;
}

SimpleAppOverlay.prototype.show = function(message, ttl, pos){
	var self = this;
	self.overlay.show();
	self.overlay.offset(self.el.offset());
	var zIndex = $(self.el).css('z-index');
	if (!zIndex || zIndex == 'auto') {
		zIndex = 1;
		$(self.el).css('z-index', zIndex);
	}
	zIndex++;
	self.overlay.css({
		width: $('body').width(),
		height: $('body').height(),
		backgroundColor: 'rgba(30,30,30, 0.1)',
		color: 'rgb(50,50,50)',
		zIndex: zIndex
	});
	if (message) self.message(message, pos);
	if (ttl) {
		if (self.displayTimer) clearTimeout(self.displayTimer)
		self.displayTimer = setTimeout(function(){
			self.hide();
		}, ttl);
	}
	return self;
}

SimpleAppOverlay.prototype.hide = function(){
	var self = this;
	if (self.displayTimer) clearTimeout(self.displayTimer)
	self.overlay.hide();
	self.message('');
	return self;
}

SimpleAppOverlay.prototype.message = function(message, pos){
	var pos = pos || {left:10, top:10};

	if (typeof message == 'string'){
		this.overlay.html(message);
	} else {
		var messageNode = $(message);
		var realMessageNode = messageNode.clone(true)
		if (messageNode.is('*')){
			this.overlay.html('').append(realMessageNode);
			realMessageNode.offset(pos);
		}
	}
	return this;
}

SimpleAppProto.prototype.itemsChanged = function(newItems, layoutChangingProperties){
	var self = this
		, itemPropertiesChangingLayout = layoutChangingProperties || ['uri']
		, changed = false

	if (self.items.length != newItems.length){
		changed = true;
	} else {
		for (var i=0;i<self.items.length;i++){
			if (utils.propertiesChanged(self.items[i], newItems[i], itemPropertiesChangingLayout)){
				changed = true;
				break;
			} else {
				_.extend(self.items[i], newItems[i])
			}
		}
	}
	return changed;
}

SimpleAppProto.prototype.propsChanged = function(newProps, layoutChangingProperties){
	var self = this
		, layoutChangingProperties = layoutChangingProperties || []
		, diff = utils.difference(self.props, newProps)
		, changed = _.keys(diff).length ? true : false

	if (changed && layoutChangingProperties.length){
		changed = utils.propertiesChanged(self.props, newProps, layoutChangingProperties);
	}

	return changed
}

SimpleAppProto.prototype.viewportOverlay = function(){
	this._viewportOverlay = this._viewportOverlay || new SimpleAppOverlay(this.el);
	return this._viewportOverlay;
}

SimpleAppProto.prototype.override = function(key, value){
	var data = {}
	if (typeof key == 'object'){
		data = key;
	} else {
		data[key] = value;
	}
	this.updateSettings({items:_.clone(this.items), props:_.extend(_.clone(this.props), data)})
}
