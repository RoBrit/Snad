/*
 * ModInformation.java
 *
 * Copyright (c) 2016 TheRoBrit
 *
 * SNAD is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SNAD is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.robrit.snad.common.util;

public class ModInformation {

  public static final String MOD_ID = "snad";
  public static final String MOD_NAME = "Snad";
  public static final String MOD_VERSION = "@VERSION@";
  public static final String MOD_DEPENDENCIES = "";
  public static final String MOD_FINGERPRINT = "@FINGERPRINT@";
  public static final String MOD_PACKAGE = "com.robrit.snad";

  public static final String PROXY_CLIENT_LOCATION = MOD_PACKAGE + ".client.proxy.ClientProxy";
  public static final String PROXY_SERVER_LOCATION = MOD_PACKAGE + ".server.proxy.ServerProxy";

  public static final boolean DEBUG_MODE = false;
}
