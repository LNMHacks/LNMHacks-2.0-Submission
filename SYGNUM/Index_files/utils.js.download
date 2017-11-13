(function(SCOPE) {
	var utils = {}
	var module = {exports: utils};

	/**
	 * @description Browser-friendly inheritance fully compatible with standard node.js inherits.
	 * @link https://github.com/isaacs/inherits
	 */
	if (typeof Object.create === 'function') {
		// implementation from standard node.js 'util' module
		module.exports.inherits = function inherits(ctor, superCtor) {
			ctor.super_ = superCtor
			ctor.prototype = Object.create(superCtor.prototype, {
				constructor: {
					value: ctor,
					enumerable: false,
					writable: true,
					configurable: true
				}
			});
		};
	} else {
		// old school shim for old browsers
		module.exports.inherits = function inherits(ctor, superCtor) {
			ctor.super_ = superCtor
			var TempCtor = function () {
			}
			TempCtor.prototype = superCtor.prototype
			ctor.prototype = new TempCtor()
			ctor.prototype.constructor = ctor
		}
	}

	function isExternalUrl(url) {
		return (/(^https?)|(^data)|(^blob)/).test(url);
	}

	module.exports.getVerticalColumnImageSize = function (viewPortWidth, imageWidth, imageHeight) {
		var ratio = imageWidth / imageHeight;

		return {
			width: ~~(viewPortWidth),
			height: ~~(viewPortWidth / ratio)
		}
	}

	module.exports.pyramid = (function simplePyramid() {
		var delta = 150;
		var lastW = $(window).innerWidth();
		var lastH = $(window).innerHeight();


		return {
			isOverThresholdChange: function () {
				var w = $(window).innerWidth();
				var h = $(window).innerHeight();

				if (Math.abs(lastW - w) > delta || Math.abs(lastH - h) > delta) {
					lastW = w;
					lastH = h;
					return true;
				}
				return false;
			}
		};
	}())

	// Crockford's Remedial JavaScript
	// http://javascript.crockford.com/remedial.html 
	// https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/String/quote
	module.exports.quote = function (str) {
		var c, i, l = str.length, o = '"';
		for (i = 0; i < l; i += 1) {
			c = str.charAt(i);
			if (c >= ' ') {
				if (c === '\\' || c === '"') {
					o += '\\';
				}
				o += c;
			} else {
				switch (c) {
					case '\b':
						o += '\\b';
						break;
					case '\f':
						o += '\\f';
						break;
					case '\n':
						o += '\\n';
						break;
					case '\r':
						o += '\\r';
						break;
					case '\t':
						o += '\\t';
						break;
					default:
						c = c.charCodeAt();
						o += '\\u00' + Math.floor(c / 16).toString(16) +
							(c % 16).toString(16);
				}
			}
		}
		return o + '"';
	}

	module.exports.decamelize = function (str) {
		var result = str.replace(/(.)([A-Z])/g, '$1 $2');
		return result.charAt(0).toUpperCase() + result.slice(1);
	}


	module.exports.fontStacks = {
		"Arial": "Arial, \"Helvetica Neue\", Helvetica, sans-serif",
		"Arial Black": "\"Arial Black\", \"Arial Bold\", Gadget, sans-serif",
		"Arial Narrow": "\"Arial Narrow\", Arial, sans-serif",
		"Arial Rounded MT Bold": "\"Arial Rounded MT Bold\", \"Helvetica Rounded\", Arial, sans-serif",
		"Avant Garde": "\"Avant Garde\", Avantgarde, \"Century Gothic\", CenturyGothic, \"AppleGothic\", sans-serif",
		"Calibri": "Calibri, Candara, Segoe, \"Segoe UI\", Optima, Arial, sans-serif",
		"Candara": "Candara, Calibri, Segoe, \"Segoe UI\", Optima, Arial, sans-serif",
		"Century Gothic": "\"Century Gothic\", CenturyGothic, AppleGothic, sans-serif",
		"Franklin Gothic Medium": "\"Franklin Gothic Medium\", \"Franklin Gothic\", \"ITC Franklin Gothic\", Arial, sans-serif",
		"Futura": "Futura, \"Trebuchet MS\", Arial, sans-serif",
		"Geneva": "Geneva, Tahoma, Verdana, sans-serif",
		"Gill Sans": "\"Gill Sans\", \"Gill Sans MT\", Calibri, sans-serif",
		"Helvetica": "\"Helvetica Neue\", Helvetica, Arial, sans-serif",
		"Impact": "Impact, Haettenschweiler, \"Franklin Gothic Bold\", Charcoal, \"Helvetica Inserat\", \"Bitstream Vera Sans Bold\", \"Arial Black\", sans serif",
		"Lucida Grande": "\"Lucida Grande\", \"Lucida Sans Unicode\", \"Lucida Sans\", Geneva, Verdana, sans-serif",
		"Optima": "Optima, Segoe, \"Segoe UI\", Candara, Calibri, Arial, sans-serif",
		"Segoe UI": "\"Segoe UI\", Frutiger, \"Frutiger Linotype\", \"Dejavu Sans\", \"Helvetica Neue\", Arial, sans-serif",
		"Tahoma": "Tahoma, Verdana, Segoe, sans-serif",
		"Trebuchet MS": "\"Trebuchet MS\", \"Lucida Grande\", \"Lucida Sans Unicode\", \"Lucida Sans\", Tahoma, sans-serif",
		"Verdana": "Verdana, Geneva, sans-serif",
		"Baskerville": "Baskerville, \"Baskerville Old Face\", \"Hoefler Text\", Garamond, \"Times New Roman\", serif",
		"Big Caslon": "\"Big Caslon\", \"Book Antiqua\", \"Palatino Linotype\", Georgia, serif",
		"Bodoni MT": "\"Bodoni MT\", Didot, \"Didot LT STD\", \"Hoefler Text\", Garamond, \"Times New Roman\", serif",
		"Book Antiqua": "\"Book Antiqua\", Palatino, \"Palatino Linotype\", \"Palatino LT STD\", Georgia, serif",
		"Calisto MT": "\"Calisto MT\", \"Bookman Old Style\", Bookman, \"Goudy Old Style\", Garamond, \"Hoefler Text\", \"Bitstream Charter\", Georgia, serif",
		"Cambria": "Cambria, Georgia, serif",
		"Didot": "Didot, \"Didot LT STD\", \"Hoefler Text\", Garamond, \"Times New Roman\", serif",
		"Garamond": "Garamond, Baskerville, \"Baskerville Old Face\", \"Hoefler Text\", \"Times New Roman\", serif",
		"Georgia": "Georgia, Times, \"Times New Roman\", serif",
		"Goudy Old Style": "\"Goudy Old Style\", Garamond, \"Big Caslon\", \"Times New Roman\", serif",
		"Hoefler Text": "\"Hoefler Text\", \"Baskerville old face\", Garamond, \"Times New Roman\", serif",
		"Lucida Bright": "\"Lucida Bright\", Georgia, serif",
		"Palatino": "Palatino, \"Palatino Linotype\", \"Palatino LT STD\", \"Book Antiqua\", Georgia, serif",
		"Perpetua": "Perpetua, Baskerville, \"Big Caslon\", \"Palatino Linotype\", Palatino, \"URW Palladio L\", \"Nimbus Roman No9 L\", serif",
		"Rockwell": "Rockwell, \"Courier Bold\", Courier, Georgia, Times, \"Times New Roman\", serif",
		"Rockwell Extra Bold": "\"Rockwell Extra Bold\", \"Rockwell Bold\", monospace",
		"Times New Roman": "TimesNewRoman, \"Times New Roman\", Times, Baskerville, Georgia, serif",
		"Andale Mono": "\"Andale Mono\", AndaleMono, monospace",
		"Consolas": "Consolas, monaco, monospace",
		"Courier New": "\"Courier New\", Courier, \"Lucida Sans Typewriter\", \"Lucida Typewriter\", monospace",
		"Lucida Console": "\"Lucida Console\", \"Lucida Sans Typewriter\", Monaco, \"Bitstream Vera Sans Mono\", monospace",
		"Lucida Sans Typewriter": "\"Lucida Sans Typewriter\", \"Lucida Console\", Monaco, \"Bitstream Vera Sans Mono\", monospace",
		"Monaco": "Monaco, Consolas, \"Lucida Console\", monospace",
		"Copperplate": "Copperplate, \"Copperplate Gothic Light\", fantasy",
		"Papyrus": "Papyrus, fantasy",
		"Brush Script MT": "\"Brush Script MT\", cursive"
	};
	module.exports.fontStacks['Helvetica Neue'] = module.exports.fontStacks['Helvetica'];
	module.exports.fontStacks['Lucida Sans Console'] = module.exports.fontStacks['Lucida Sans Typewriter'];
	module.exports.fontFamilyDegradation = function (fontFamily) {
		if (!fontFamily) return;
		var normalizedFontFamily = utils.decamelize(fontFamily);
		if (utils.fontStacks[normalizedFontFamily]) {
			return utils.fontStacks[normalizedFontFamily];
		} else if (utils.fontStacks[fontFamily]) {
			return utils.fontStacks[fontFamily];
		} else if (/\s/.test(normalizedFontFamily)) {
			// http://mathiasbynens.be/notes/unquoted-font-family
			// http://www.w3.org/TR/CSS2/fonts.html#font-family-prop
			return [utils.quote(normalizedFontFamily), fontFamily].join(', ')
		} else {
			return [normalizedFontFamily, fontFamily].join(', ')
		}
	}

	module.exports.hexToRgba = function (hexColor, opacity) {
		if (!hexColor) return;
		var resultColor
			, color = (hexColor.charAt(0) == '#') ? hexColor.slice(1) : hexColor
			, r = parseInt(color.substring(0, 2), 16)
			, g = parseInt(color.substring(2, 4), 16)
			, b = parseInt(color.substring(4, 6), 16)
			, a = isNaN(opacity) ? 1 : parseFloat(opacity);

		if (opacity == 1 || isNaN(opacity)) {
			resultColor = 'rgb(' + r + ',' + g + ',' + b + ')'
		} else {
			resultColor = 'rgba(' + r + ',' + g + ',' + b + ',' + a + ')';
		}

		return resultColor
	}

	module.exports.capabilities = {
		mask: function () {
			var root = document.getElementsByTagName('html')[0];
			var rootClassParts = root.className ? root.className.split(' ') : []

			if (document.body.style['-webkit-mask-repeat'] !== undefined) {
				rootClassParts.push('cssmasks');
				root.className = rootClassParts.join(' ');
				return true
			} else {
				rootClassParts.push('no-cssmasks');
				root.className = rootClassParts.join(' ');
				return false
			}

		}
	}

	module.exports.difference = function (source, target) {
		var val, changed = {};
		for (var attr in target) {
			if (_.isEqual(source[attr], (val = target[attr]))) continue;
			changed[attr] = val;
		}
		return changed;
	}

	module.exports.propertiesChanged = function (source, target, properties) {
		var source = source || {}
			, target = target || {}
			, properties = properties || []

		return _.keys(_.pick(utils.difference(source, target), properties)).length ? true : false
	}

	module.exports.getRandomInt = function (min, max) {
		return Math.floor(Math.random() * (max - min + 1)) + min;
	};

	module.exports.getResizedImageUrlSrz =  function(relativeUrl, width, height, sharpParams) {
		// assign shap default parameters
		sharpParams = sharpParams || {};
		sharpParams.quality = sharpParams.quality || 75;
		sharpParams.resizeFilter = sharpParams.resizaFilter || 22;
		sharpParams.usm_r = sharpParams.usm_r || 0.50;
		sharpParams.usm_a = sharpParams.usm_a || 1.20;
		sharpParams.usm_t = sharpParams.usm_t || 0.00;

		if (isExternalUrl(relativeUrl)){
			return relativeUrl;
		}

		var urlArr = [];
		var splitUrl = /[.]([^.]+)$/.exec(relativeUrl);
		var ext = (splitUrl && /[.]([^.]+)$/.exec(relativeUrl)[1]) || "";

		// build the image url
		relativeUrl = 'https://static.wixstatic.com/media/' + relativeUrl;
		urlArr.push(relativeUrl);
		urlArr.push("srz");
		urlArr.push("p");
		urlArr.push(width);
		urlArr.push(height);

		// sharpening parameters
		urlArr.push(sharpParams.quality);
		urlArr.push(sharpParams.resizaFilter);
		urlArr.push(sharpParams.usm_r);
		urlArr.push(sharpParams.usm_a);
		urlArr.push(sharpParams.usm_t);

		urlArr.push(ext); // get file extension
		urlArr.push("srz");

		return urlArr.join('_');
	},


	module.exports.getResizedImageFacedCroppedUrl = function (relativeUrl, width, height) {
		if (isExternalUrl(relativeUrl)){
			return relativeUrl;
		} else {
			return "//static.wixstatic.com/media/" + relativeUrl + "/v1/fill/w_"+ width + ",h_" + height + ",al_fs,q_75,usm_0.50_1.20_0.00/" + relativeUrl;
		}
	};

	module.exports.isEqualQuality = function (qualityA, qualityB) {
		qualityA = qualityA || {}
		qualityB = qualityB || {}
		qualityA.unsharpMask = qualityA.unsharpMask || {}
		qualityB.unsharpMask = qualityB.unsharpMask || {}
		return (
			(qualityA.quality === qualityB.quality) &&
			(qualityA.unsharpMask.radius === qualityB.unsharpMask.radius) &&
			(qualityA.unsharpMask.amount === qualityB.unsharpMask.amount) &&
			(qualityA.unsharpMask.threshold === qualityB.unsharpMask.threshold)
		);
	}

	module.exports.getResizedImageUrl = function (relativeUrl, targetWidth, targetHeight, params) {
		// IMPORTANT: duplicated in Wix.js (same function name)

		if (isExternalUrl(relativeUrl)) {
			return relativeUrl;
		}
		params = params || {};
		params.siteQuality = params.siteQuality || {};
		params.siteQuality.unsharpMask = params.siteQuality.unsharpMask || {};
		//siteQuality list [
		//	{unsharpMask: {radius: 0.0, amount: 0, threshold: 0.00}, quality: 90},
		//	{unsharpMask: {radius: 1.0, amount: 1.5, threshold: 0.04}}
		//]

		var PAR = (window.devicePixelRatio || 1);
		var widthWithPar = targetWidth * PAR, heightWithPar = targetHeight * PAR;
		if ( (widthWithPar <= params.maxWidth) && (heightWithPar <= params.maxHeight) ) {
			targetWidth = widthWithPar;
			targetHeight = heightWithPar;
		}

		var quality = params.siteQuality.quality;
		// Calculate default quality
		if (!quality) {
			var HIGH_QUALITY_RESOLUTION = 1400 * 1400;
			var MEDIUM_QUALITY_RESOLUTION = 600 * 600;
			var dimension = targetWidth * targetHeight;
			if (dimension > HIGH_QUALITY_RESOLUTION) {
				quality = 90;
			} else if (dimension > MEDIUM_QUALITY_RESOLUTION) {
				quality = 85;
			} else {
				quality = 80;
			}
		}

		// assign sharp default parameters
		params.resizaFilter = params.resizaFilter || 22;

		var site_usm = params.siteQuality.unsharpMask;
		var usm_r =  (typeof site_usm.radius !== 'undefined' ? site_usm.radius : 0.66);
		var usm_a = (typeof site_usm.amount !== 'undefined' ? site_usm.amount : 1.00);
		var usm_t = (typeof site_usm.threshold !== 'undefined' ? site_usm.threshold : 0.01);
		var isValidUSM = ((usm_r >= 0.1 && usm_r <= 500) && (usm_a >= 0 && usm_a <= 10) && (usm_t >=0 && usm_t <= 255));

		var retUrl = 'https://static.wixstatic.com/media/'+ relativeUrl + '/v1/fill/';
		retUrl += 'w_' + Math.round(targetWidth);
		retUrl += ',h_' + Math.round(targetHeight);
		retUrl += ',al_c';
		retUrl += ',q_' + quality;
		if (isValidUSM) {
			retUrl += ',usm_' + usm_r.toFixed(2) + '_' + usm_a.toFixed(2) + '_' + usm_t.toFixed(2);
		}
		retUrl += '/' + relativeUrl;
		return retUrl;

	};


	module.exports.loadGoogleFontIfNeeded = function(propsFont) {
		if (!this.loadedFonts) {
			this.loadedFonts = {};
		}
		if (this.loadedFonts[propsFont]) return; //already loaded

		this.loadedFonts[propsFont] = true;
		Wix.Styles.getEditorFonts(function (fonts) {
			var selected = false;
			fonts.forEach(function (fontObj) {
				selected = _.find(fontObj.fonts, {fontFamily: propsFont, provider: 'google'})
			});
			if (selected) {
				WebFont.load({
					google: {
						families: [selected.cdnName]
					}
				});
			}
		});
	};

	module.exports.insertCssRule = function(self, rule) {
		if (!self.tmpStyleSheet) {
			var dynamicStyleNode = $('<style></style>').appendTo('head').attr('id', 'dynamic-style').get(0);
			self.tmpStyleSheet = _.find(document.styleSheets, function(sheet){return sheet.ownerNode === dynamicStyleNode})

			if (!self.tmpStyleSheet) { // fallback
				for (var i = document.styleSheets.length - 1; i >= 0; i--) {
					var style = document.styleSheets[i];
					if (style.cssRules != null) {
						self.tmpStyleSheet = style;
						break;
					}
				}
			}
		}


		var s = self.tmpStyleSheet, l= 0, rule;
		if (s.insertRule){
			l = s.cssRules.length
			s.insertRule(rule, l);
			rule = s.cssRules[l];
		} else if (s.addRule){
			l = s.rules.length
			s.addRule(rule, l);
			rule = s.rules[l];
		}
		return rule;
		//rule.style.backgroundColor = overlayColor;
		//if (!$.support.opacity){
		//	rule.style['-ms-filter']="progid:DXImageTransform.Microsoft.Alpha(Opacity=50)";
		//	rule.style['filter']="alpha(opacity=50)";
		//	rule.style.opacity = 0.5;
		//}

	}
	
	SCOPE.utils = utils;
	
}(window));
